package com.resumeanalyzer.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.resumeanalyzer.backend.entity.Resume;
import com.resumeanalyzer.backend.entity.ResumeAnalysis;
import com.resumeanalyzer.backend.entity.User;

public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {
    Optional<ResumeAnalysis> findByResume(Resume resume);

    @Query("""
            SELECT MAX(ra.atsScore)
            FROM ResumeAnalysis ra
            WHERE ra.resume.user = :user
            """)
    Integer findBestScoreByUser(@Param("user") User user);
}
