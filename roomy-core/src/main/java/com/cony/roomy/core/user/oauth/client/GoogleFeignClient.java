package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.common.config.OAuthFeignClientConfig;
import com.cony.roomy.core.user.oauth.response.GoogleTokenResponse;
import com.cony.roomy.core.user.oauth.response.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "GoogleApi", url = "url-placeholder-to-dynamic", configuration = OAuthFeignClientConfig.class)
public interface GoogleFeignClient {

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    GoogleTokenResponse getTokens(URI uri, @RequestBody MultiValueMap<String, ?> body);

    @GetMapping(value = "/oauth2/v3/userinfo", consumes = "application/x-www-form-urlencoded")
    GoogleUserInfo getUserInfo(URI url, @RequestHeader("Authorization") String accessToken);
}
