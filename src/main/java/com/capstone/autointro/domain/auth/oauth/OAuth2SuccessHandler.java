package com.capstone.autointro.domain.auth.oauth;

import com.capstone.autointro.common.jwt.CookieProvider;
import com.capstone.autointro.common.jwt.JwtProvider;
import com.capstone.autointro.common.jwt.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 프론트 콜백 URI - 이 페이지에서 /api/auth/reissue 호출하여 Access Token 획득
    private static final String REDIRECT_URI = "http://localhost:3000/oauth2/callback";

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getUserId();

        String refreshToken = jwtProvider.generateRefreshToken(userId);

        // Redis에 저장
        refreshTokenRepository.save(userId, refreshToken);

        // HttpOnly 쿠키에 세팅
        cookieProvider.addRefreshTokenCookie(response, refreshToken);

        // Access Token은 URL 노출 없이 프론트 콜백 URI로만 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URI);
    }
}
