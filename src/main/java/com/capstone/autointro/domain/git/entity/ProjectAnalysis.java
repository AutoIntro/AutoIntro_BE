package com.capstone.autointro.domain.git.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long id;

    @Column(name = "description", columnDefinition = "text")
    private String description;         // 프로젝트 구체적인 설명

    @Column(name = "tech_stack", columnDefinition = "text")
    private String techStack;           // 기술 스택 설명

    @Column(name = "project_effect", columnDefinition = "text")
    private String projectEffect;       // 프로젝트가 가지는 효과

    @Column(name = "troubleshooting", columnDefinition = "text")
    private String troubleshooting;     // 트러블슈팅 & 해결과정

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id", nullable = false)
    private Project project;

    public void update(String description, String techStack, String projectEffect, String troubleshooting) {
        this.description = description;
        this.techStack = techStack;
        this.projectEffect = projectEffect;
        this.troubleshooting = troubleshooting;
    }
}
