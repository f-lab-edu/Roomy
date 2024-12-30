package com.cony.roomy.core.user.domain;

import com.cony.roomy.core.common.config.JwtProperties;
import com.cony.roomy.core.common.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class TokenRepository {

    private final String REFRESH_PREFIX = "token:refresh:";
    private final String BLACKLIST_PREFIX = "token:black:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;

    public void saveRefreshToken(Long userId, String refreshToken) {
        // 해당 유저의 refresh 토큰이 이미 저장되어 있는 경우, 새로운 refresh token 발급을 위해 삭제
        if(hasRefreshToken(userId)) {
            deleteRefreshToken(userId);
        }
        redisTemplate.opsForValue()
                .set(REFRESH_PREFIX + userId, refreshToken,
                        Duration.ofMillis(TimeUtil.getTimeToLive(jwtProperties.getRefreshTokenTTL())));
    }

    public boolean hasRefreshToken(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(REFRESH_PREFIX + userId));
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(REFRESH_PREFIX + userId);
    }

    public void saveBlackList(String token) {
        redisTemplate.opsForValue()
                .set(BLACKLIST_PREFIX + token, "EXPIRED",
                        Duration.ofMillis(TimeUtil.getTimeToLive(jwtProperties.getAccessTokenTTL())));

    }

    public boolean hasBlackList(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
