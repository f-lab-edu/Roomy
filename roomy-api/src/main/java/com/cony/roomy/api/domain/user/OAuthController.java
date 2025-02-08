package com.cony.roomy.api.domain.user;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.user.dto.response.TokenResponse;
import com.cony.roomy.core.user.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/login/oauth2/callback/{provider}")
    public ApiResponse<TokenResponse> oAuthLogin(@PathVariable String provider, @RequestParam("code") String authorizationCode) {
        TokenResponse tokenResponse = oAuthService.login(provider, authorizationCode);

        return ApiResponse.ok(tokenResponse);
    }
}
