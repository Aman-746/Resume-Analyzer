package com.resumeanalyzer.backend.dto;

import java.time.LocalDateTime;

import com.resumeanalyzer.backend.enums.AnalysisStatus;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeResponse {
    private Long id;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private AnalysisStatus analysisStatus;
}
