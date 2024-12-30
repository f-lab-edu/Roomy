package com.cony.roomy.core.common.config;

import com.cony.roomy.core.common.filter.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.swing.*;

/**
 * 스프링 web mvc 설정 변경을 위한 web config, 인증 인터셉트 추가
 */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/**");
//        registry.addInterceptor(authInterceptor)
//                .excludePathPatterns(
//                        // user
//                        "/api/terms",
//                        "/api/terms/**",
//                        "/api/sms/**",
//                        "/register",
//                        "/login"
//                );
    }
}
