package com.capstone.autointro.domain.introduction.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Introduction API", description = "자기소개서 관련 API")
@Validated
@RequestMapping("/api/introductions")
public interface IntroductionApi {

    @Operation(summary = "내 자기소개서 목록 조회", description = "로그인한 유저의 자기소개서 목록을 조회합니다.")
    @GetMapping("/me")
    ResponseEntity<ApiResponse<?>> getMyIntroductions(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "자기소개서 조회", description = "자기소개서 내용을 조회합니다.")
    @GetMapping("/{introductionId}")
    ResponseEntity<ApiResponse<?>> getIntroduction(
            @Parameter(description = "자기소개서 ID") @Positive @PathVariable Long introductionId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "자기소개서 수정", description = "자기소개서를 수정합니다.")
    @PutMapping("/{introductionId}")
    ResponseEntity<ApiResponse<?>> updateIntroduction(
            @Parameter(description = "자기소개서 ID") @Positive @PathVariable Long introductionId,
            @RequestBody Object request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "자기소개서 삭제", description = "자기소개서를 삭제합니다.")
    @DeleteMapping("/{introductionId}")
    ResponseEntity<ApiResponse<?>> deleteIntroduction(
            @Parameter(description = "자기소개서 ID") @Positive @PathVariable Long introductionId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "자기소개서 AI 재작성 요청", description = "AI한테 자기소개서를 다시 작성하도록 요청합니다.")
    @PostMapping("/{introductionId}/reorganize")
    ResponseEntity<ApiResponse<?>> reorganizeIntroduction(
            @Parameter(description = "자기소개서 ID") @Positive @PathVariable Long introductionId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "수정 전 AI 자기소개서 조회", description = "회원이 수정하기 전 AI가 생성한 원본 자기소개서를 조회합니다.")
    @GetMapping("/ai/{introductionId}")
    ResponseEntity<ApiResponse<?>> getAiIntroduction(
            @Parameter(description = "자기소개서 ID") @Positive @PathVariable Long introductionId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
