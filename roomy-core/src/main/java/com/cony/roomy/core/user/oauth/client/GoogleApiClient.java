package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.user.oauth.config.GoogleProperties;
import com.cony.roomy.core.user.oauth.config.OAuthConstant;
import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;
import com.cony.roomy.core.user.oauth.response.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleApiClient implements OAuthApiClient {

    private final GoogleProperties googleProperties;
    private final GoogleFeignClient googleFeignClient;

    @Override
    public String getOAuthToken(String authorizationCode) {
        String url = googleProperties.getAuthUrl();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("grant_type", OAuthConstant.GRANT_TYPE);
        body.add("client_id", googleProperties.getClientId());
        body.add("client_secret", googleProperties.getClientSecret());
        body.add("redirect_uri", googleProperties.getRedirectUrl());

        GoogleTokenResponse googleTokenResponse = googleFeignClient.getTokens(URI.create(url), body);

        return googleTokenResponse.getAccessToken();
    }

    @Override
    public OAuthUserInfo getOAuthUserInfo(String accessToken) {
        String url = googleProperties.getApiUrl();
        String authorizationHeader = "Bearer " + accessToken;

        return googleFeignClient.getUserInfo(URI.create(url), authorizationHeader);
    }
}
