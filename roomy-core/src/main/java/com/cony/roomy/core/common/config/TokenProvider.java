package com.cony.roomy.core.common.config;

import com.cony.roomy.core.common.util.TimeUtil;
import com.cony.roomy.core.user.domain.TokenRepository;
import com.cony.roomy.core.user.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.cony.roomy.core.common.config.TokenProvider.TokenType.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final TokenRepository tokenRepository;

    enum TokenType {
        AT, RT
    }

    public String createAccessToken(User user) {
        long accessTokenTTL = TimeUtil.getTimeToLive(jwtProperties.getAccessTokenTTL());
        return generateToken(user, accessTokenTTL, AT);
    }

    public String createRefreshToken(User user) {
        long refreshTokenTTL = TimeUtil.getTimeToLive(jwtProperties.getRefreshTokenTTL());
        return generateToken(user, refreshTokenTTL, RT);
    }

    private String generateToken(User user, long expireTime, TokenType tokenType) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setSubject(user.getEmail())
                .setExpiration(new Date(now.getTime() + expireTime))
                .claim("id", user.getUserId())
                .claim("token_type", tokenType.name())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean hasWhiteList(String accessToken) {
        return tokenRepository.hasWhiteList(accessToken);
    }

    // 토큰으로 유저 PK 가져오기
    public Long getUserIdByToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
