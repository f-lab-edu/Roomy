package com.cony.roomy.core.user.oauth.response;

import com.cony.roomy.core.user.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserInfo implements OAuthUserInfo {

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String nickname;

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.GOOGLE;
    }
}
