package com.capstone.autointro.domain.auth.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API")
@Validated
@RequestMapping("/api/auth")
public interface AuthApi {

    @Operation(summary = "토큰 재발급", description = "액세스 토큰 만료 시 쿠키의 리프레시 토큰으로 재발급합니다.")
    @PostMapping("/reissue")
    ResponseEntity<ApiResponse<?>> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    );

    @Operation(summary = "로그아웃", description = "리프레시 토큰 쿠키를 삭제하여 로그아웃 처리합니다.")
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<?>> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
