package com.capstone.autointro.domain.resume.entity;

import com.capstone.autointro.common.base.BaseEntity;
import com.capstone.autointro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "resume_url", nullable = false, length = 255)
    private String resumeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}
