package com.capstone.autointro.domain.auth.controller;

import com.capstone.autointro.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API")
@Validated
@RequestMapping("/api/auth")
public interface AuthApi {

    @SecurityRequirements
    @Operation(
            summary = "액세스 토큰 재발급",
            description = """
                    쿠키에 저장된 Refresh Token을 이용해 새로운 Access Token을 발급합니다.

                    **소셜 로그인 완료 후 프론트에서 이 API를 호출하여 Access Token을 획득합니다.**

                    - Refresh Token은 HttpOnly 쿠키(`refresh_token`)로 자동 전송됩니다.
                    - 성공 시 새로운 Access Token이 응답 `result` 필드에 반환됩니다.
                    - Refresh Token도 함께 갱신(Rotation)됩니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "AUTH_200_2",
                                      "message": "토큰 재발급 성공입니다.",
                                      "result": "eyJhbGciOiJIUzI1NiJ9..."
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh Token 없음 또는 만료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/reissue")
    ResponseEntity<ApiResponse<?>> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    );

    @Operation(
            summary = "로그아웃",
            description = """
                    Redis에서 Refresh Token을 삭제하고 쿠키를 만료시켜 로그아웃 처리합니다.

                    - 요청 헤더에 `Authorization: Bearer {accessToken}` 필요합니다.
                    - 로그아웃 후 기존 Refresh Token은 재사용 불가합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "AUTH_200_1",
                                      "message": "로그아웃 성공입니다.",
                                      "result": null
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<?>> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
