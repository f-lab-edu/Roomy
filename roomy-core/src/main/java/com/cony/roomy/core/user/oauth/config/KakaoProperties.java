package com.cony.roomy.core.user.oauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource("classpath:application-oauth.properties")
public class KakaoProperties implements OAuthProperties {

    @Value("${oauth.kakao.client-id}")
    private String clientId;

//    @Value("${oauth.kakao.client-secret}")
//    private String clientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUrl;

    @Value("${oauth.kakao.auth-url}")
    private String authUrl;

    @Value("${oauth.kakao.api-url}")
    private String apiUrl;

}
