package com.cony.roomy.core.user.oauth.response;

import com.cony.roomy.core.user.oauth.OAuthProvider;

public interface OAuthUserInfo {

    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
