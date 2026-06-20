package com.resumeanalyzer.backend.entity;

import java.time.LocalDateTime;

import com.resumeanalyzer.backend.enums.AnalysisStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="original_file_name", nullable = false, length=150)
    private String originalFileName;

    @Column(name="stored_file_name", nullable=false, length=150)
    private String storedFileName;

    @Column(name = "file_path", nullable = false, length=255)
    private String filePath;

    @Column(name="content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name="file_size", nullable = false)
    private Long fileSize;

    @Column(name="uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="analysis_status", nullable = false)
    private AnalysisStatus analysisStatus;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

}
