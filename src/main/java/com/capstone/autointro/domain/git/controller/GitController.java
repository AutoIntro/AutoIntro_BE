package com.capstone.autointro.domain.git.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.domain.git.service.GitCommandService;
import com.capstone.autointro.domain.git.service.GitQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class GitController implements GitApi {

    private final GitQueryService gitQueryService;
    private final GitCommandService gitCommandService;

    @Override
    public ResponseEntity<ApiResponse<?>> getProjectList(Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> saveProject(Object request, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateProject(Long gitId, Object request, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> reorganizeProject(Long gitId, Long userId) {
        // TODO: 구현
        return null;
    }
}
