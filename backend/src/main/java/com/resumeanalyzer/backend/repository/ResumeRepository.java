package com.resumeanalyzer.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeanalyzer.backend.entity.Resume;
import com.resumeanalyzer.backend.entity.User;
import com.resumeanalyzer.backend.enums.AnalysisStatus;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUser(User user);

    Long countByUser(User user);

    Long countByUserAndAnalysisStatus(
            User user,
            AnalysisStatus analysisStatus);
}
