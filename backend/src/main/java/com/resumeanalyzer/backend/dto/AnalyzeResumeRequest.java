package com.resumeanalyzer.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AnalyzeResumeRequest {
    private String filePath;
}
