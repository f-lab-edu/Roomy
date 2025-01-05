package com.cony.roomy.core.user.oauth.response;

import com.cony.roomy.core.user.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo implements OAuthUserInfo {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;

        @JsonProperty("profile")
        private Profile profile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        @JsonProperty("nickname")
        private String nickname;
    }

    // 카카오 정책으로 인해 비즈앱 전환을 안하면 email 뽑기 불가능
    public String getEmail() {
        if (kakaoAccount == null || kakaoAccount.getEmail() == null) {
            return "test1234@test.com";
        }
        return kakaoAccount.getEmail();
    }

    public String getNickname() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) {
            return null;
        }
        return kakaoAccount.getProfile().getNickname();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
