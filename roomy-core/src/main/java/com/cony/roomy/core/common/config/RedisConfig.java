package com.cony.roomy.core.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 레디스 사용을 위한 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 레디스 커넥션 등록 시 LettuceConnectionFactory 사용해 host ip와 port 정보 파라미터로 넘겨 등록
        // 해당 정보는 application.yaml의 spring.data.redis.host / redis.port 에서 가져온다.
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory()); // Redis 연결 팩토리 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키를 문자열로 직렬화하도록 설정
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 값을 문자열로 직렬화하도록 설정
        return redisTemplate;
    }
}
