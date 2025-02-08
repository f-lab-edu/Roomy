package com.cony.roomy.core.user.oauth.client;

import com.cony.roomy.core.common.config.OAuthFeignClientConfig;
import com.cony.roomy.core.user.oauth.response.KakaoUserInfo;
import com.cony.roomy.core.user.oauth.response.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@FeignClient(name = "KakaoApi", url = "url-placeholder-to-dynamic", configuration = OAuthFeignClientConfig.class)
public interface KakaoFeignClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoTokenResponse getTokens(URI uri, @RequestBody MultiValueMap<String, ?> body);

    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded")
    KakaoUserInfo getUserInfo(URI url, @RequestHeader("Authorization") String accessToken);
}
