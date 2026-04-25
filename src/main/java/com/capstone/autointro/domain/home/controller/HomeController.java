package com.capstone.autointro.domain.home.controller;

import com.capstone.autointro.common.response.ApiResponse;
import com.capstone.autointro.domain.home.service.HomeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class HomeController implements HomeApi {

    private final HomeQueryService homeQueryService;

    @Override
    public ResponseEntity<ApiResponse<?>> getHome(Long userId) {
        // TODO: 구현
        return null;
    }
}
