package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;

public interface OAuthApiClient {

    String requestAccessToken(String authorizationCode);
    OAuthUserInfo requestOAuthUserInfo(String accessToken);
}
