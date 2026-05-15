package com.capstone.autointro.domain.introduction.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.domain.introduction.service.IntroductionCommandService;
import com.capstone.autointro.domain.introduction.service.IntroductionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class IntroductionController implements IntroductionApi {

    private final IntroductionQueryService introductionQueryService;
    private final IntroductionCommandService introductionCommandService;

    @Override
    public ResponseEntity<ApiResponse<?>> getMyIntroductions(Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getIntroduction(Long introductionId, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateIntroduction(Long introductionId, Object request, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteIntroduction(Long introductionId, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> reorganizeIntroduction(Long introductionId, Long userId) {
        // TODO: 구현
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getAiIntroduction(Long introductionId, Long userId) {
        // TODO: 구현
        return null;
    }
}
