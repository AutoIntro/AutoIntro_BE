package com.capstone.autointro.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Tag(name = "Login API", description = "소셜 로그인 API (브라우저에서 직접 접근)")
@Validated
@RequestMapping("/oauth2/authorization")
public interface LoginApi {

    @SecurityRequirements
    @Operation(
            summary = "GitHub 소셜 로그인",
            description = """
                    GitHub 계정으로 소셜 로그인을 진행합니다.

                    **주의:** Swagger에서 직접 실행할 수 없습니다. 브라우저 주소창에 URL을 직접 입력하세요.

                    **로그인 흐름:**
                    1. 브라우저에서 `GET /oauth2/authorization/github` 접근
                    2. GitHub 로그인 페이지로 리다이렉트
                    3. 로그인 완료 후 프론트 콜백 URI(`OAUTH2_REDIRECT_URI`)로 리다이렉트
                    4. 프론트에서 `POST /api/auth/reissue` 호출하여 Access Token 획득

                    **사전 설정 필요:**
                    - GitHub OAuth App → Authorization callback URL:
                      `http://localhost:8080/login/oauth2/code/github`
                    - 환경변수 `OAUTH2_REDIRECT_URI` 설정 (기본값: `http://localhost:3000/oauth2/callback`)
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "GitHub 로그인 페이지로 리다이렉트"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/github")
    void githubLogin(HttpServletResponse response) throws IOException;
}
