package com.resumeanalyzer.backend.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResumeAnalysisResponse {
    private Integer atsScore;
    private List<String>strengths;
    private List<String>weaknesses;
    private List<String>suggestions;
    private String summary;
}
