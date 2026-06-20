package com.resumeanalyzer.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.aspectj.apache.bcel.generic.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.resumeanalyzer.backend.dto.AnalyzeResumeRequest;
import com.resumeanalyzer.backend.dto.DashboardStatsResponse;
import com.resumeanalyzer.backend.dto.ResumeAnalysisResponse;
import com.resumeanalyzer.backend.dto.ResumeResponse;
import com.resumeanalyzer.backend.dto.UploadResumeResponse;
import com.resumeanalyzer.backend.entity.Resume;
import com.resumeanalyzer.backend.entity.ResumeAnalysis;
import com.resumeanalyzer.backend.entity.User;
import com.resumeanalyzer.backend.enums.AnalysisStatus;
import com.resumeanalyzer.backend.repository.ResumeAnalysisRepository;
import com.resumeanalyzer.backend.repository.ResumeRepository;
import com.resumeanalyzer.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
// It will do constructor injection by own
@RequiredArgsConstructor
public class ResumeService {

    private static final String UPLOAD_DIR = "uploads/resumes";

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final ObjectMapper objectMapper;

    public UploadResumeResponse uploadResume(MultipartFile file, String userEmail) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Resume file is empty");
            }

            if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
                throw new RuntimeException("Only PDF, DOC and DOCX files are allowed");
            }

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            String originalFileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID() + "_" + originalFileName;

            Path filePath = uploadPath.resolve(storedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("filepath: " + filePath.toAbsolutePath().toString());

            Resume resume = Resume.builder()
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .filePath(filePath.toString())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .uploadedAt(LocalDateTime.now())
                    .analysisStatus(AnalysisStatus.NOT_ANALYZED)
                    .user(user)
                    .build();

            resumeRepository.save(resume);

            return UploadResumeResponse.builder()
                    .resumeId(resume.getId())
                    .fileName(resume.getOriginalFileName())
                    .status("UPLOADED")
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume");
        }
    }

    public DashboardStatsResponse getDashboardStats(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long totalResumes = resumeRepository.countByUser(user);

        Long analysesDone = resumeRepository.countByUserAndAnalysisStatus(
                user,
                AnalysisStatus.ANALYZED);

        Integer bestScore = resumeAnalysisRepository.findBestScoreByUser(user);

        return DashboardStatsResponse.builder()
                .totalResumes(totalResumes)
                .analysesDone(analysesDone)
                .bestScore(bestScore == null ? 0 : bestScore)
                .build();
    }

    public List<ResumeResponse> getUserResumes(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Resume> resumes = resumeRepository.findByUser(user);

        return resumes.stream()
                .map(resume -> ResumeResponse.builder()
                        .id(resume.getId())
                        .originalFileName(resume.getOriginalFileName())
                        .fileSize(resume.getFileSize())
                        .uploadedAt(resume.getUploadedAt())
                        .analysisStatus(resume.getAnalysisStatus())
                        .build())
                .toList();
    }

    private ResumeAnalysisResponse mapToResponse(
            ResumeAnalysis analysis) {

        try {

            return ResumeAnalysisResponse.builder()
                    .atsScore(analysis.getAtsScore())
                    .strengths(
                            objectMapper.readValue(
                                    analysis.getStrengths(),
                                    new TypeReference<List<String>>() {
                                    }))
                    .weaknesses(
                            objectMapper.readValue(
                                    analysis.getWeaknesses(),
                                    new TypeReference<List<String>>() {
                                    }))
                    .suggestions(
                            objectMapper.readValue(
                                    analysis.getSuggestions(),
                                    new TypeReference<List<String>>() {
                                    }))
                    .summary(analysis.getSummary())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error mapping analysis", e);
        }
    }

    public ResumeAnalysisResponse getResumeAnalysis(
            Long resumeId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        ResumeAnalysis analysis = resumeAnalysisRepository
                .findByResume(resume)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));

        return mapToResponse(analysis);
    }

    @Transactional
    public ResumeAnalysisResponse analyzeResume(Long resumeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        if (resume.getAnalysisStatus() == AnalysisStatus.ANALYZED) {
            ResumeAnalysis savedAnalysis = resumeAnalysisRepository.findByResume(resume)
                    .orElseThrow(() -> new RuntimeException("Analysis not found"));

            return mapToResponse(savedAnalysis);
        }

        AnalyzeResumeRequest request = AnalyzeResumeRequest.builder()
                .filePath(Paths.get(resume.getFilePath()).toAbsolutePath().toString())
                .build();

        ResponseEntity<ResumeAnalysisResponse> response = restTemplate.postForEntity("http://localhost:5000/analyze",
                request,
                ResumeAnalysisResponse.class);

        ResumeAnalysisResponse analysis = response.getBody();

        System.out.println(analysis);

        if (analysis != null) {
            ResumeAnalysis resumeAnalysis = ResumeAnalysis.builder()
                    .resume(resume)
                    .atsScore(analysis.getAtsScore())
                    .strengths(
                            objectMapper.writeValueAsString(analysis.getStrengths()))
                    .weaknesses(
                            objectMapper.writeValueAsString(analysis.getWeaknesses()))
                    .suggestions(
                            objectMapper.writeValueAsString(analysis.getSuggestions()))
                    .summary(analysis.getSummary())
                    .analyzedAt(LocalDateTime.now())
                    .build();

            resumeAnalysisRepository.save(resumeAnalysis);

            resume.setAnalysisStatus(AnalysisStatus.ANALYZED);

            resumeRepository.save(resume);
        }

        return analysis;
    }

}
