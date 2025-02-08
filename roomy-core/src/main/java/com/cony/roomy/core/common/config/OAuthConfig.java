package com.cony.roomy.core.common.config;

import com.cony.roomy.core.user.oauth.OAuthProvider;
import com.cony.roomy.core.user.oauth.client.GoogleApiClient;
import com.cony.roomy.core.user.oauth.client.KakaoApiClient;
import com.cony.roomy.core.user.oauth.client.OAuthApiClient;
import com.cony.roomy.core.user.oauth.config.GoogleProperties;
import com.cony.roomy.core.user.oauth.config.KakaoProperties;
import com.cony.roomy.core.user.oauth.config.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class OAuthConfig {

    private final GoogleProperties googleProperties;
    private final KakaoProperties kakaoProperties;
    private final GoogleApiClient googleApiClient;
    private final KakaoApiClient kakaoApiClient;

    @Bean
    public Map<OAuthProvider, OAuthProperties> oAuthPropertiesMap() {
        Map<OAuthProvider, OAuthProperties> oAuthPropertiesMap = new HashMap<>();
        oAuthPropertiesMap.put(OAuthProvider.GOOGLE, googleProperties);
        oAuthPropertiesMap.put(OAuthProvider.KAKAO, kakaoProperties);

        return oAuthPropertiesMap;
    }

    @Bean
    public Map<OAuthProvider, OAuthApiClient> oAuthApiClientMap() {
        Map<OAuthProvider, OAuthApiClient> oAuthApiClientMap = new HashMap<>();
        oAuthApiClientMap.put(OAuthProvider.GOOGLE, googleApiClient);
        oAuthApiClientMap.put(OAuthProvider.KAKAO, kakaoApiClient);

        return oAuthApiClientMap;
    }

}
