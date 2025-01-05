package com.cony.roomy.core.user.service;

import com.cony.roomy.core.common.config.TokenProvider;
import com.cony.roomy.core.user.domain.User;
import com.cony.roomy.core.user.domain.UserRepository;
import com.cony.roomy.core.user.dto.response.TokenResponse;
import com.cony.roomy.core.user.oauth.OAuthProvider;
import com.cony.roomy.core.user.oauth.client.OAuthApiClient;
import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final Map<OAuthProvider, OAuthApiClient> oAuthApiClientMap;
    private final UserRepository userRepository;
    private final LoginService loginService;

    public TokenResponse login(String provider, String authorizationCode) {
        OAuthUserInfo oAuthUserInfo = request(provider, authorizationCode);
        // 보통의 경우 새로운 유저라면 자사 회원에 맞는 페이지가 있어야 하나, 일단은 임시로 바로 저장되도록 설정
        User user = findOrCreateUser(oAuthUserInfo);
        return loginService.generateToken(user);
    }

    // OAuth Provider 통해 로그인 시도하고 유저 정보 가져오기
    public OAuthUserInfo request(String provider, String authorizationCode) {
        OAuthProvider oAuthProvider = OAuthProvider.valueOf(provider.toUpperCase());
        OAuthApiClient oAuthApiClient = oAuthApiClientMap.get(oAuthProvider);
        String accessToken = oAuthApiClient.requestAccessToken(authorizationCode);
        return oAuthApiClient.requestOAuthUserInfo(accessToken);
    }

    private User findOrCreateUser(OAuthUserInfo oAuthUserInfo) {
        return userRepository.findByEmail(oAuthUserInfo.getEmail())
                .orElseGet(() -> createUser(oAuthUserInfo));
    }

    private User createUser(OAuthUserInfo oAuthUserInfo) {
        User user = User.builder()
                .email(oAuthUserInfo.getEmail())
                .encryptPassword("salt:" + UUID.randomUUID())
                .nickname(oAuthUserInfo.getNickname())
                .build();

        userRepository.save(user);
        return user;
    }
}
