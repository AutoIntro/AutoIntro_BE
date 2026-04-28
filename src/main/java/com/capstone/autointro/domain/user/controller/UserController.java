package com.capstone.autointro.domain.user.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.domain.user.service.UserCommandService;
import com.capstone.autointro.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController implements UserApi {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Override
    public ResponseEntity<ApiResponse<?>> getMyInfo(Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> createProfile(Object request, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateUser(Object request, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteUser(Long userId) {
        // TODO: 구현
        return null;
    }
}
