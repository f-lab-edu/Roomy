package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.user.oauth.config.KakaoProperties;
import com.cony.roomy.core.user.oauth.config.OAuthConstant;
import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;
import com.cony.roomy.core.user.oauth.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class KakaoApiClient implements OAuthApiClient {

    private final KakaoProperties kakaoProperties;
    private final KakaoFeignClient kakaoFeignClient;

    @Override
    public String getOAuthToken(String authorizationCode) {
        String url = kakaoProperties.getAuthUrl();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("grant_type", OAuthConstant.GRANT_TYPE);
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_url", kakaoProperties.getRedirectUrl());

        KakaoTokenResponse kakaoTokenResponse = kakaoFeignClient.getTokens(URI.create(url), body);

        return kakaoTokenResponse.getAccessToken();
    }

    @Override
    public OAuthUserInfo getOAuthUserInfo(String accessToken) {
        String url = kakaoProperties.getApiUrl();
        String authorizationHeader = "Bearer " + accessToken;
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");
        return kakaoFeignClient.getUserInfo(URI.create(url), authorizationHeader);

    }
}
