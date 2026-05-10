package com.capstone.autointro.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoIntro API")
                        .description("""
                                ## AutoIntro 백엔드 API 명세서

                                ### GitHub 소셜 로그인 흐름
                                1. 브라우저 주소창에 직접 입력 **(Swagger 실행 불가)**
                                   - `GET /oauth2/authorization/github`
                                2. GitHub 로그인 완료 후 프론트 콜백으로 리다이렉트
                                3. 프론트에서 `POST /api/auth/reissue` 호출 → **Access Token** 획득
                                4. 이후 모든 인증 요청에 헤더 첨부
                                   - `Authorization: Bearer {accessToken}`

                                ### Swagger에서 인증 API 테스트 방법
                                1. 위 로그인 흐름으로 Access Token 획득
                                2. 우측 상단 **Authorize** 버튼 클릭
                                3. Access Token 값만 입력 (Bearer 접두사 제외)

                                ### 참고
                                - Access Token 유효시간: **45분**
                                - Refresh Token 유효시간: **14일** (HttpOnly 쿠키, Rotation 방식)
                                - Refresh Token은 Redis에 저장되며 재사용 공격 방어 적용
                                """)
                        .version("v1.0.0"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 서버")
                ))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("JWT Access Token을 입력하세요. (Bearer 접두사 제외)")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
