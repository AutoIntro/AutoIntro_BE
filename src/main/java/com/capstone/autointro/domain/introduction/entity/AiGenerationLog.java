package com.capstone.autointro.domain.introduction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_generation_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiGenerationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @Column(name = "full_prompt", nullable = false, columnDefinition = "text")
    private String fullPrompt;

    @Column(name = "prompt_version", nullable = false, length = 10)
    private String promptVersion;

    @Column(name = "total_tokens", nullable = false)
    private Integer totalTokens;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_ed", nullable = false)
    private UserRequest userRequest;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
