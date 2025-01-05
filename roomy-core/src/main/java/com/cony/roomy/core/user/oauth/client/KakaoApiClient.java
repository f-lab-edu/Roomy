package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.user.oauth.config.KakaoProperties;
import com.cony.roomy.core.user.oauth.config.OAuthConstant;
import com.cony.roomy.core.user.oauth.response.KakaoUserInfo;
import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;
import com.cony.roomy.core.user.oauth.tokens.KakaoTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class KakaoApiClient implements OAuthApiClient {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    @Override
    public String requestAccessToken(String authorizationCode) {
        String url = kakaoProperties.getAuthUrl() + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("grant_type", OAuthConstant.GRANT_TYPE);
        body.add("client_id", kakaoProperties.getClientId());

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);
        Objects.requireNonNull(response);

        return response.getAccessToken();
    }

    @Override
    public OAuthUserInfo requestOAuthUserInfo(String accessToken) {
        String url = kakaoProperties.getApiUrl() + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(url, request, KakaoUserInfo.class);
    }
}
