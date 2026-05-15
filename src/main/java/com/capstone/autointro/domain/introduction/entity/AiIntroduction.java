package com.capstone.autointro.domain.introduction.entity;

import com.capstone.autointro.common.base.BaseEntity;
import com.capstone.autointro.domain.introduction.enums.IntroductionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_introductions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiIntroduction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_intro_id")
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntroductionStatus status;

    @Column(name = "field", length = 100)
    private String field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_ed", nullable = false)
    private UserRequest userRequest;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(IntroductionStatus status) {
        this.status = status;
    }
}
