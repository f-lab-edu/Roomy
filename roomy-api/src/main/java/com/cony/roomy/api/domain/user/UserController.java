package com.cony.roomy.api.domain.user;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.common.filter.Authentication;
import com.cony.roomy.core.user.dto.request.AddUserRequest;
import com.cony.roomy.core.user.dto.request.LoginRequest;
import com.cony.roomy.core.user.dto.request.LogoutRequest;
import com.cony.roomy.core.user.dto.request.TokenRequest;
import com.cony.roomy.core.user.dto.response.TokenResponse;
import com.cony.roomy.core.user.dto.response.UserResponse;
import com.cony.roomy.core.user.service.LoginService;
import com.cony.roomy.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid AddUserRequest addUserRequest) {
        userService.register(addUserRequest);
        return ApiResponse.created("회원가입 완료");
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.ok(loginService.login(loginRequest));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        return ApiResponse.ok(loginService.reissue(tokenRequest));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) {
        loginService.logout(logoutRequest);
        return ApiResponse.okOnlyMessage("로그아웃 완료");
    }

    @Authentication
    @GetMapping("/api/user")
    public ApiResponse<UserResponse> getUser() {
        return ApiResponse.ok(userService.getUser());
    }

}
