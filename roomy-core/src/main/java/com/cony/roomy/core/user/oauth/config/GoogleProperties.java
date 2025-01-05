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
public class GoogleProperties implements OAuthProperties {

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUrl;

    @Value("${oauth.google.auth-url}")
    private String authUrl;

    @Value("${oauth.google.api-url}")
    private String apiUrl;

}
