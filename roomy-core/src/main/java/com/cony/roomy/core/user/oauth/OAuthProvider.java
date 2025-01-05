package com.cony.roomy.core.user.oauth;

import com.cony.roomy.core.user.oauth.config.OAuthProperties;

public enum OAuthProvider {
    GOOGLE {
        @Override
        public String generateAuthorizeCodeUrl(OAuthProperties oAuthProperties) {
            return "https://accounts.google.com/o/oauth2/v2/auth?" +
                    "scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email&" +
                    "access_type=offline&" +
                    "response_type=code&" +
                    "state=state_parameter_passthrough_value&" +
                    "redirect_uri=" + oAuthProperties.getRedirectUrl() + "&" +
                    "client_id=" + oAuthProperties.getClientId();
        }
    },
    KAKAO {
        @Override
        public String generateAuthorizeCodeUrl(OAuthProperties oAuthProperties) {
            return "https://kauth.kakao.com/oauth/authorize?" +
                    "response_type=code&" +
                    "client_id=" + oAuthProperties.getClientId() + "&" +
                    "redirect_uri=" + oAuthProperties.getRedirectUrl();
        }
    };

    abstract public String generateAuthorizeCodeUrl(OAuthProperties oAuthProperties);
}
