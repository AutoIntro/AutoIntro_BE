package com.capstone.autointro.domain.user.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "회원 관련 API")
@Validated
@RequestMapping("/api/users")
public interface UserApi {

    @Operation(summary = "내 페이지 조회", description = "로그인한 회원의 정보를 조회합니다.")
    @GetMapping("/me")
    ResponseEntity<ApiResponse<?>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "최초 정보 기입", description = "최초 로그인 시 회원 프로필 정보를 등록합니다.")
    @PostMapping("/profile")
    ResponseEntity<ApiResponse<?>> createProfile(
            @RequestBody Object request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "회원 상태 변경", description = "회원 상태를 변경합니다.")
    @PutMapping("/me")
    ResponseEntity<ApiResponse<?>> updateUser(
            @RequestBody Object request,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );

    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제(탈퇴) 처리합니다.")
    @DeleteMapping("/me")
    ResponseEntity<ApiResponse<?>> deleteUser(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
