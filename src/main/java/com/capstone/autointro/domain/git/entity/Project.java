package com.capstone.autointro.domain.git.entity;

import com.capstone.autointro.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repo_id")
    private Long id;

    @Column(name = "repo_name", nullable = false, length = 255)
    private String repoName;

    @Column(name = "repo_url", nullable = false, length = 255)
    private String repoUrl;

    @Column(name = "main_lang", nullable = false, length = 255)
    private String mainLang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String repoName, String repoUrl, String mainLang) {
        this.repoName = repoName;
        this.repoUrl = repoUrl;
        this.mainLang = mainLang;
    }
}
