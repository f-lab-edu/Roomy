package com.cony.roomy.core.common.filter;

import com.cony.roomy.core.common.config.TokenProvider;
import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.common.security.SecurityContextHolder;
import com.cony.roomy.core.user.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Map;

/**
 * Dispatcher Servlet를 통해 요청이 컨트롤러에 도달하기 전 인터셉트를 활용해 인증 진행하는 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Authentication 어노테이션이 붙어있지 않으면 토큰 검증 X
        if(!isAuthenticationNeed(handler)) {
            return true;
        }
        // 시큐리티 컨텍스트에 이미 유저 컨텍스트가 포함되어 있으면 토큰 검증 X
        if(SecurityContextHolder.hasContext()) {
            return true;
        }

        final String authorization = request.getHeader("Authorization");
        final String accessToken = getBearerToken(authorization);
        // AT 토큰 유효성 검사
        if(!tokenProvider.isValid(accessToken)) {
            throw new RoomyException(ErrorType.AUTH_AT_TOKEN_INVALID, Map.of("access token", accessToken), log::warn);
        }
        // 화이트 리스트에서 AT 토큰 검색
        if(!tokenProvider.hasWhiteList(accessToken)) {
            throw new RoomyException(ErrorType.AUTH_AT_TOKEN_NOT_IN_WHITELIST, Map.of("access token", accessToken), log::warn);
        }

        final Long userId = tokenProvider.getUserIdByToken(accessToken);
        // 스프링 시큐리티의 SecurityContextHolder -> Context -> Authentication 과 같은 ThreadLocal context 에 저장
        SecurityContextHolder.setContext(loginService.loadUserById(userId));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        SecurityContextHolder.clear();
    }

    private String getBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new RoomyException(ErrorType.AUTH_TOKEN_NOT_BEARER, Map.of("authorization", authorization != null ? authorization : "null"), log::warn);
        }
        return authorization.substring("Bearer ".length());
    }

    private boolean isAuthenticationNeed(final Object handler) {
        // favicon 제외
        if (handler instanceof ResourceHttpRequestHandler) {
            return false;
        }

        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handlerMethod.hasMethodAnnotation(Authentication.class)) {
            return true;
        }
        return false;
    }
}
