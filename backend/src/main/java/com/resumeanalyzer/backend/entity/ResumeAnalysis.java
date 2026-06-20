package com.resumeanalyzer.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resume_analysis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Integer atsScore;

    @Lob
    private String strengths;

    @Lob
    private String weaknesses;

    @Lob
    private String suggestions;

    @Lob
    private String summary;

    private LocalDateTime analyzedAt;

    @OneToOne
    @JoinColumn(name="resume_id", nullable=false, unique = true)
    private Resume resume;
}
