package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.user.oauth.response.OAuthUserInfo;

public interface OAuthApiClient {

    String getOAuthToken(String authorizationCode);
    OAuthUserInfo getOAuthUserInfo(String accessToken);
}
