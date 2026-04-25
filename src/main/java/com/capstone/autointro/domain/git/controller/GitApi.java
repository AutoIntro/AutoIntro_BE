package com.capstone.autointro.domain.git.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Git API", description = "깃 프로젝트 관련 API")
@Validated
@RequestMapping("/api/git")
public interface GitApi {

    @Operation(summary = "깃 프로젝트 목록 조회", description = "로그인한 유저의 깃 프로젝트 목록을 조회합니다.")
    @GetMapping
    ResponseEntity<ApiResponse<?>> getProjectList(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 저장", description = "깃 프로젝트를 저장합니다.")
    @PostMapping
    ResponseEntity<ApiResponse<?>> saveProject(
            @RequestBody Object request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 수정", description = "깃 프로젝트를 직접 수정합니다.")
    @PutMapping("/{gitId}")
    ResponseEntity<ApiResponse<?>> updateProject(
            @Parameter(description = "깃 프로젝트 ID") @Positive @PathVariable Long gitId,
            @RequestBody Object request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "깃 프로젝트 AI 재정리", description = "깃 프로젝트를 AI한테 다시 정리하도록 요청합니다.")
    @PostMapping("/{gitId}/reorganize")
    ResponseEntity<ApiResponse<?>> reorganizeProject(
            @Parameter(description = "깃 프로젝트 ID") @Positive @PathVariable Long gitId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
