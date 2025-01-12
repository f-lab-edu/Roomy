package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.user.oauth.OAuthProvider;
import com.cony.roomy.core.user.oauth.config.GoogleProperties;
import com.cony.roomy.core.user.oauth.config.OAuthConstant;
import com.cony.roomy.core.user.oauth.response.GoogleUserInfo;
import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;
import com.cony.roomy.core.user.oauth.tokens.GoogleTokens;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleApiClient implements OAuthApiClient {

    private final GoogleProperties googleProperties;
    private final RestTemplate restTemplate;

    @Override
    public String requestAccessToken(String authorizationCode) {
        String url = googleProperties.getAuthUrl() + "/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("grant_type", OAuthConstant.GRANT_TYPE);
        body.add("client_id", googleProperties.getClientId());
        body.add("client_secret", googleProperties.getClientSecret());
        body.add("redirect_uri", googleProperties.getRedirectUrl());

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        // TODO: feign client -> 특징, 변경
        GoogleTokens response = restTemplate.postForObject(url, request, GoogleTokens.class);
        Objects.requireNonNull(response);

        return response.getAccessToken();
    }

    @Override
    public OAuthUserInfo requestOAuthUserInfo(String accessToken) {
        String url = googleProperties.getApiUrl() + "/oauth2/v3/userinfo";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        Objects.requireNonNull(response.getBody());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), GoogleUserInfo.class);
        } catch (Exception e) {
            throw new RoomyException(ErrorType.OAUTH_USER_PARSING_INVALID,
                    Map.of("Authorization Server", OAuthProvider.GOOGLE.name(), "response", response.getBody()), log::warn);
        }

    }
}
