package com.capstone.autointro.common.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final String KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    // 저장: refresh:{userId} = refreshToken
    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                key(userId),
                refreshToken,
                Duration.ofMillis(refreshTokenExpiration)
        );
    }

    // 조회
    public Optional<String> find(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key(userId)));
    }

    // 삭제 (로그아웃)
    public void delete(Long userId) {
        redisTemplate.delete(key(userId));
    }

    // 일치 여부 검증
    public boolean isValid(Long userId, String refreshToken) {
        return find(userId)
                .map(stored -> stored.equals(refreshToken))
                .orElse(false);
    }

    private String key(Long userId) {
        return KEY_PREFIX + userId;
    }
}
