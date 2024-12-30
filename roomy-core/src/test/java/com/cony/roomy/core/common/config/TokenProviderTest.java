package com.cony.roomy.core.common.config;

import com.cony.roomy.core.user.domain.User;
import com.cony.roomy.core.user.domain.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 토큰 만료 기간을 전달해 토큰을 생성한다")
    @Test
    @Transactional
    void test_토큰생성() {
        //given
        User user = User.builder()
                .email("test234@gmail.com")
                .encryptPassword("test")
                .build();
        userRepository.save(user);
        //when
        String token = tokenProvider.createAccessToken(user);

        //then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(user.getUserId());
    }

    @DisplayName("validToken(): 만료된 토큰이면 유효성 검증에 실패한다")
    @Test
    void test_토큰만료() throws Exception {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);
        //when
        boolean result = tokenProvider.isValid(token);
        //then
        assertThat(result).isFalse();

    }

    @DisplayName("getUserIdByToken(): 토큰으로 유저 PK 조회한다")
    @Test
    void test_토큰으로유저PK조회() throws Exception {
        //given
        Long userId = 10L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);
        //when
        Long userIdByToken = tokenProvider.getUserIdByToken(token);

        //then
        assertThat(userId).isEqualTo(userIdByToken);
    }

}