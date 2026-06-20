package com.resumeanalyzer.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name", nullable = false, length=100)
    private String fullName;

    @Column(nullable = false, unique = true, length=150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

}
