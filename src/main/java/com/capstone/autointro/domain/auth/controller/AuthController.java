package com.capstone.autointro.domain.auth.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.common.status.success.SuccessStatus;
import com.capstone.autointro.domain.auth.service.AuthCommandService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController implements AuthApi {

    private final AuthCommandService authCommandService;

    @Override
    public ResponseEntity<ApiResponse<?>> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String newAccessToken = authCommandService.reissueToken(request, response);
        return ApiResponse.of(SuccessStatus.TOKEN_REISSUE_SUCCESS, newAccessToken);
    }

    @Override
    public ResponseEntity<ApiResponse<?>> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Long userId
    ) {
        authCommandService.logout(request, response, userId);
        return ApiResponse.of(SuccessStatus.LOGOUT_SUCCESS);
    }
}
