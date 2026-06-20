package com.resumeanalyzer.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resumeanalyzer.backend.dto.DashboardStatsResponse;
import com.resumeanalyzer.backend.dto.ResumeAnalysisResponse;
import com.resumeanalyzer.backend.dto.ResumeResponse;
import com.resumeanalyzer.backend.dto.UploadResumeResponse;
import com.resumeanalyzer.backend.service.ResumeService;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResumeResponse> uploadResume(@RequestParam("resume") MultipartFile resume) {

        String userEmail = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UploadResumeResponse response = resumeService.uploadResume(resume, userEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        DashboardStatsResponse response = resumeService.getDashboardStats(email);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getUserResumes() {
        String userEmail = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<ResumeResponse> resumes = resumeService.getUserResumes(userEmail);

        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/{resumeId}/analysis")
    public ResponseEntity<ResumeAnalysisResponse> getResumeAnalysis(@PathVariable Long resumeId) {
        String userEmail = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        ResumeAnalysisResponse response = resumeService.getResumeAnalysis(resumeId, userEmail);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{resumeId}/analyze")
    public ResponseEntity<ResumeAnalysisResponse> analyzeResume(@PathVariable Long resumeId) {
        String userEmail = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        ResumeAnalysisResponse response = resumeService.analyzeResume(resumeId, userEmail);

        return ResponseEntity.ok(response);
    }
}
