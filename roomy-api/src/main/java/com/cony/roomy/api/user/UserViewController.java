package com.cony.roomy.api.user;

import com.cony.roomy.core.user.oauth.OAuthProvider;
import com.cony.roomy.core.user.oauth.config.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    private final Map<OAuthProvider, OAuthProperties> oAuthPropertiesMap;

    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

    @GetMapping("/login/oauth/{provider}")
    public String authorizationCode(@PathVariable String provider) {
        OAuthProvider oAuthProvider = OAuthProvider.valueOf(provider.toUpperCase());
        String url = oAuthProvider.generateAuthorizeCodeUrl(oAuthPropertiesMap.get(oAuthProvider));

        return "redirect:" + url;
    }
}
