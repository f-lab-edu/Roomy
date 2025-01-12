package com.cony.roomy.core.user.oauth.client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "KakaoApi", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {
}
