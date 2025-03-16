package com.cony.roomy.core.common.config.property;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@PropertySource("classpath:application-sms.properties")
public class SmsProperties {

    @Value("${cool-sms.key}")
    private String key;

    @Value("${cool-sms.secret}")
    private String secret;

    @Value("${cool-sms.number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        log.debug("API Key: {}", key);
        log.debug("API Secret: {}", secret);
        log.debug("From Number: {}", fromNumber);
    }
}
