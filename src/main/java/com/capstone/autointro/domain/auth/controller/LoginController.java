package com.capstone.autointro.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginController implements LoginApi {

    @Override
    public void githubLogin(HttpServletResponse response) throws IOException {
        // Spring Security OAuth2가 /oauth2/authorization/github 요청을 직접 처리하므로
        // 이 메서드는 Swagger 문서화 목적으로만 존재합니다.
        response.sendRedirect("/oauth2/authorization/github");
    }
}
