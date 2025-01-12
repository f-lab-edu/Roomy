package com.cony.roomy.core.user.oauth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
public class KakaoTokenRequest {

    @JsonProperty("code")
    private String code;

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("redirect_url")
    private String redirectUrl;

}
