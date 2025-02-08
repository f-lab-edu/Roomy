package com.cony.roomy.core.user.service;

import com.cony.roomy.core.common.config.TokenProvider;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.user.domain.TokenRepository;
import com.cony.roomy.core.user.domain.User;
import com.cony.roomy.core.user.domain.UserRepository;
import com.cony.roomy.core.user.dto.request.LoginRequest;
import com.cony.roomy.core.user.dto.request.LogoutRequest;
import com.cony.roomy.core.user.dto.request.TokenRequest;
import com.cony.roomy.core.user.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public User loadUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RoomyException(ErrorType.USER_NOT_FOUND, Map.of("userId", userId), log::warn));
    }

    public TokenResponse login(LoginRequest loginRequest) {
        // 유저 이메일 확인
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RoomyException(ErrorType.USER_NOT_FOUND, Map.of("email", loginRequest.email()), log::warn));
        // 유저 비밀번호 확인
        if(!passwordEncoder.matches(loginRequest.password(), user.getEncryptPassword())) {
            throw new RoomyException(ErrorType.USER_INVALID_PASSWORD, Map.of("enter password", loginRequest.password()), log::warn);
        }

        return generateToken(user);
    }

    public TokenResponse generateToken(User user) {
        // 유저 토큰 발급
        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);
        // refresh 토큰 저장
        tokenRepository.saveRefreshToken(user.getUserId(), refreshToken);
        // access token 화이트 리스트에 저장
        tokenRepository.saveWhiteList(user.getUserId(), accessToken);
        return TokenResponse.of(accessToken, refreshToken);
    }

    // access token 재발급 비즈니스 로직
    public TokenResponse reissue(TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.refreshToken();
        // 1. refresh 토큰 유효성 검사
        if(!tokenProvider.isValid(refreshToken)) {
            throw new RoomyException(ErrorType.AUTH_RT_TOKEN_INVALID, Map.of("refresh token", refreshToken), log::warn);
        }
        // 2. user id 유효성 검사
        Long userId = tokenProvider.getUserIdByToken(refreshToken);
        // 3. RT 토큰 저장소에서 확인
        if(!tokenRepository.hasRefreshToken(userId)) {
            throw new RoomyException(ErrorType.AUTH_RT_TOKEN_EXPIRED, Map.of("refresh token", refreshToken), log::warn);
        }
        String tokenMatch = tokenRepository.findRefreshToken(userId);
        if(!tokenMatch.equals(refreshToken)) {
            throw new RoomyException(ErrorType.AUTH_RT_TOKEN_INVALID, Map.of("refresh token", refreshToken), log::warn);
        }
        // 4. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RoomyException(ErrorType.USER_NOT_FOUND, Map.of("user id", userId), log::warn));
        String accessToken = tokenProvider.createAccessToken(user);
        // 5. AT 토큰 화이트 리스트에 저장
        tokenRepository.saveWhiteList(user.getUserId(), accessToken);

        return TokenResponse.reissue(accessToken);
    }

    // 로그아웃
    public void logout(LogoutRequest logoutRequest) {
        final Long userId = tokenProvider.getUserIdByToken(logoutRequest.accessToken());
        // 1. AT 토큰 화이트 리스트에서 삭제
        tokenRepository.deleteWhiteList(userId);
        // 2. RT 토큰 레디스에서 삭제
        tokenRepository.deleteRefreshToken(userId);
    }

}
