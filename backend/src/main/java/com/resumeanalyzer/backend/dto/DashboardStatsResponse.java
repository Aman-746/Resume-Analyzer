package com.resumeanalyzer.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {

    private Long totalResumes;

    private Long analysesDone;

    private Integer bestScore;
}