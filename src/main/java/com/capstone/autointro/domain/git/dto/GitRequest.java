package com.capstone.autointro.domain.git.dto;

import jakarta.validation.constraints.NotBlank;

public class GitRequest {

    /**
     * fullRepoName: "username/repo-name" 형식
     */
    public record Save(
            @NotBlank(message = "레포지토리 전체 이름은 필수입니다. (예: username/repo-name)") String fullRepoName,
            @NotBlank(message = "주요 언어는 필수입니다.") String mainLang
    ) {}

    public record Update(
            @NotBlank(message = "레포지토리 이름은 필수입니다.") String repoName,
            @NotBlank(message = "레포지토리 URL은 필수입니다.") String repoUrl,
            @NotBlank(message = "주요 언어는 필수입니다.") String mainLang
    ) {}

    /**
     * additionalRequest: 사용자가 AI 재정리 시 추가로 요청하고 싶은 내용 (선택)
     * 예: "트러블슈팅을 JWT 인증 관련 내용 위주로 작성해줘"
     */
    public record Reorganize(
            String additionalRequest
    ) {}
}
