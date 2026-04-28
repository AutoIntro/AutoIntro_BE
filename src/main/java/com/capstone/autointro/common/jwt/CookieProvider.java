package com.capstone.autointro.common.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieProvider {

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${cookie.refresh-token-name}")
    private String refreshTokenName;

    @Value("${cookie.domain}")
    private String domain;

    @Value("${cookie.same-site}")
    private String sameSite;

    @Value("${cookie.secure}")
    private boolean secure;

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(refreshTokenName, refreshToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(domain)
                .path("/")
                .maxAge(refreshTokenExpiration / 1000)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(refreshTokenName, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .domain(domain)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> refreshTokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
