package com.capstone.autointro.domain.home.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Home API", description = "홈 화면 관련 API")
@Validated
@RequestMapping("/api/home")
public interface HomeApi {

    @Operation(summary = "메인 화면 조회", description = "홈 화면에 필요한 데이터를 조회합니다.")
    @GetMapping
    ResponseEntity<ApiResponse<?>> getHome(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
