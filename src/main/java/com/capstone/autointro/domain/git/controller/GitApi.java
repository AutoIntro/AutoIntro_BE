package com.capstone.autointro.domain.git.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.domain.git.dto.GitRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Git API", description = "깃 프로젝트 관련 API")
@Validated
@RequestMapping("/api/git")
public interface GitApi {

    @Operation(summary = "GitHub 레포지토리 목록 조회", description = "로그인한 GitHub 계정의 모든 레포지토리를 GitHub에서 직접 가져옵니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 GitHub 토큰 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "GitHub API 오류")
    })
    @GetMapping("/github")
    ResponseEntity<ApiResponse<?>> getGithubRepoList(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 목록 조회", description = "로그인한 유저의 깃 프로젝트 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "GIT_200",
                                      "message": "깃 프로젝트 목록 조회 성공입니다.",
                                      "result": {
                                        "projects": [
                                          {
                                            "projectId": 1,
                                            "repoName": "my-project",
                                            "repoUrl": "https://github.com/user/my-project",
                                            "mainLang": "Java"
                                          }
                                        ],
                                        "totalCount": 1
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping
    ResponseEntity<ApiResponse<?>> getProjectList(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 저장", description = """
                    GitHub 레포지토리를 분석하여 저장합니다.
                    
                    **처리 순서:**
                    1. GitHub API → 레포지토리 정보 조회
                    2. GitHub API → 최근 커밋 50개 조회
                    3. OpenAI → 커밋 기반 기술스택 / 트러블슈팅 요약
                    4. DB 저장 (Project + CommitLog + ProjectAnalysis)
                    
                    **fullRepoName 형식:** `username/repo-name`
                    """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "GIT_201",
                                      "message": "깃 프로젝트 저장 성공입니다.",
                                      "result": {
                                        "projectId": 1,
                                        "repoName": "my-project",
                                        "repoUrl": "https://github.com/username/my-project",
                                        "mainLang": "Java"
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 값 또는 잘못된 fullRepoName 형식"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 GitHub 토큰 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "로그인 계정과 레포 소유자 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "GitHub API 또는 AI 요약 실패")
    })
    @PostMapping
    ResponseEntity<ApiResponse<?>> saveProject(
            @Valid @RequestBody GitRequest.Save request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 수정", description = "깃 프로젝트를 직접 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "GIT_200_2",
                                      "message": "깃 프로젝트 수정 성공입니다.",
                                      "result": {
                                        "projectId": 1,
                                        "repoName": "updated-project",
                                        "repoUrl": "https://github.com/user/updated-project",
                                        "mainLang": "Kotlin"
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 프로젝트에 대한 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @PutMapping("/{gitId}")
    ResponseEntity<ApiResponse<?>> updateProject(
            @Parameter(description = "깃 프로젝트 ID") @Positive @PathVariable Long gitId,
            @Valid @RequestBody GitRequest.Update request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 AI 재정리", description = """
                    GitHub 레포지토리 데이터를 다시 가져와 AI로 재분석합니다.

                    **additionalRequest** (선택): 사용자가 원하는 추가 요청사항을 자유롭게 작성할 수 있습니다.
                    - 예: "트러블슈팅을 JWT 인증 관련 내용 위주로 작성해줘"
                    - 예: "기술 스택에 Redis와 Docker를 강조해줘"
                    - 예: "자소서에서 팀 협업 경험이 드러나도록 작성해줘"

                    추가 요청사항이 없으면 Body 없이 호출해도 됩니다.
                    """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "AI 재정리 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "GIT_200_3",
                                      "message": "깃 프로젝트 AI 재정리 성공입니다.",
                                      "result": {
                                        "projectId": 1,
                                        "repoName": "my-project",
                                        "description": "이 프로젝트는 ...",
                                        "techStack": "Spring Boot, JPA, MySQL ...",
                                        "projectEffect": "사용자에게 ...",
                                        "troubleshooting": "1. 문제 상황: ..."
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "해당 프로젝트에 대한 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "프로젝트 또는 분석 결과를 찾을 수 없음")
    })
    @PostMapping("/{gitId}/reorganize")
    ResponseEntity<ApiResponse<?>> reorganizeProject(
            @Parameter(description = "깃 프로젝트 ID") @Positive @PathVariable Long gitId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "추가 요청사항 (선택, 없으면 Body 생략 가능)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "additionalRequest": "트러블슈팅을 JWT 인증 관련 내용 위주로 작성해줘"
                                    }
                                    """)
                    )
            )
            @RequestBody(required = false) GitRequest.Reorganize request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
