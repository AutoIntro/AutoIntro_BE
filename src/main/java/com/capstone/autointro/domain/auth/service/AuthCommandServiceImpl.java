package com.capstone.autointro.domain.auth.service;

import com.capstone.autointro.common.exception.GeneralException;
import com.capstone.autointro.common.jwt.CookieProvider;
import com.capstone.autointro.common.jwt.JwtProvider;
import com.capstone.autointro.common.jwt.RefreshTokenRepository;
import com.capstone.autointro.common.status.error.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 Refresh Token 추출
        String refreshToken = cookieProvider.extractRefreshToken(request)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRESH_TOKEN_MISSING));

        // 서명 + 만료 검증
        jwtProvider.validateToken(refreshToken);

        // 타입 검증
        if (!jwtProvider.isRefreshToken(refreshToken)) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        // Redis 저장값과 일치 여부 검증 - 탈취된 토큰 차단
        if (!refreshTokenRepository.isValid(userId, refreshToken)) {
            // 불일치 시 해당 유저 Refresh Token 전체 삭제 (재사용 공격 방어)
            refreshTokenRepository.delete(userId);
            cookieProvider.deleteRefreshTokenCookie(response);
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND);
        }

        // 새 토큰 발급
        String newAccessToken = jwtProvider.generateAccessToken(userId);
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        // Refresh Token Rotation - Redis + 쿠키 교체
        refreshTokenRepository.save(userId, newRefreshToken);
        cookieProvider.addRefreshTokenCookie(response, newRefreshToken);

        return newAccessToken;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
        // Redis에서 Refresh Token 삭제
        refreshTokenRepository.delete(userId);

        // 쿠키 삭제
        cookieProvider.deleteRefreshTokenCookie(response);
    }
}
