package com.cony.roomy.core.common.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource("classpath:application-jwt.properties")
public class JwtProperties {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-ttl}")
    private String accessTokenTTL;

    @Value("${jwt.refresh-token-ttl}")
    private String refreshTokenTTL;

}
