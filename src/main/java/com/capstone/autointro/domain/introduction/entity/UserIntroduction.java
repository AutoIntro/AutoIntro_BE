package com.capstone.autointro.domain.introduction.entity;

import com.capstone.autointro.common.base.BaseEntity;
import com.capstone.autointro.domain.introduction.enums.IntroductionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_introduction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserIntroduction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_intro_id")
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntroductionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_ed", nullable = false)
    private UserRequest userRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_intro_id", nullable = false)
    private AiIntroduction aiIntroduction;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(IntroductionStatus status) {
        this.status = status;
    }
}
