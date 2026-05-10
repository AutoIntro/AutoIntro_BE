package com.capstone.autointro.domain.git.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.common.status.success.SuccessStatus;
import com.capstone.autointro.domain.git.dto.GitRequest;
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
    public ResponseEntity<ApiResponse<?>> getGithubRepoList(Long userId) {
        return ApiResponse.of(SuccessStatus.PROJECT_LIST_SUCCESS,
                gitQueryService.getGithubRepoList(userId));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getProjectList(Long userId) {
        return ApiResponse.of(SuccessStatus.PROJECT_LIST_SUCCESS,
                gitQueryService.getProjectList(userId));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> saveProject(GitRequest.Save request, Long userId) {
        return ApiResponse.of(SuccessStatus.PROJECT_SAVED,
                gitCommandService.saveProject(request, userId));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateProject(Long gitId, GitRequest.Update request, Long userId) {
        return ApiResponse.of(SuccessStatus.PROJECT_UPDATED,
                gitCommandService.updateProject(gitId, request, userId));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> reorganizeProject(Long gitId, GitRequest.Reorganize request, Long userId) {
        return ApiResponse.of(SuccessStatus.PROJECT_REORGANIZE_SUCCESS,
                gitCommandService.reorganizeProject(gitId, request, userId));
    }
}
