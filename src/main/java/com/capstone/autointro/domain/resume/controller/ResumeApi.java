package com.capstone.autointro.domain.resume.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Resume API", description = "이력서 관련 API")
@Validated
@RequestMapping("/api/resume")
public interface ResumeApi {

    @Operation(summary = "이력서 목록 조회", description = "로그인한 유저의 이력서 목록을 조회합니다.")
    @GetMapping
    ResponseEntity<ApiResponse<?>> getResumeList(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "이력서 상세 조회", description = "이력서 상세 내용을 조회합니다.")
    @GetMapping("/{resumeId}")
    ResponseEntity<ApiResponse<?>> getResume(
            @Parameter(description = "이력서 ID") @Positive @PathVariable Long resumeId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
