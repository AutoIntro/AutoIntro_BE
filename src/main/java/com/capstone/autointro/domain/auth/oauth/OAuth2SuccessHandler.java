package com.capstone.autointro.domain.auth.oauth;

import com.capstone.autointro.common.jwt.CookieProvider;
import com.capstone.autointro.common.jwt.JwtProvider;
import com.capstone.autointro.common.jwt.RefreshTokenRepository;
import com.capstone.autointro.domain.auth.repository.ProviderRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth2.redirect-uri}")
    private String redirectUri;

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProviderRepository providerRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = oAuth2User.getUserId();

        // GitHub Access Token 저장
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "github", authentication.getName()
        );
        if (authorizedClient != null) {
            String githubAccessToken = authorizedClient.getAccessToken().getTokenValue();
            providerRepository.findByUserIdAndProviderName(userId, "github")
                    .ifPresent(provider -> provider.updateGithubAccessToken(githubAccessToken));
        }

        String refreshToken = jwtProvider.generateRefreshToken(userId);
        refreshTokenRepository.save(userId, refreshToken);
        cookieProvider.addRefreshTokenCookie(response, refreshToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
