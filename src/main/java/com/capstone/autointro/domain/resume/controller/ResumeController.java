package com.capstone.autointro.domain.resume.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.domain.resume.service.ResumeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ResumeController implements ResumeApi {

    private final ResumeQueryService resumeQueryService;

    @Override
    public ResponseEntity<ApiResponse<?>> getResumeList(Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getResume(Long resumeId, Long userId) {
        // TODO: 구현
        return null;
    }
}
