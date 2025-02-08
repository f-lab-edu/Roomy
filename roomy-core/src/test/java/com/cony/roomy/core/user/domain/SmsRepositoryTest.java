package com.cony.roomy.core.user.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
class SmsRepositoryTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Configuration
    static class TestRedisConfig {
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory("localhost", 6379);
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(connectionFactory);
            redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키를 문자열로 직렬화하도록 설정
            redisTemplate.setValueSerializer(new StringRedisSerializer()); // 값을 문자열로 직렬화하도록 설정
            return redisTemplate;
        }
    }

    @DisplayName("레디스에 값을 저장한다")
    @Test
    public void test_저장() throws InterruptedException {
        //given
        String phoneNumber = "01012345678";
        String code = "1234";
        String key = "sms:" + phoneNumber;

        //when
        redisTemplate.opsForValue().set(key, code, 3, TimeUnit.SECONDS);

        //then
        Boolean isKey = redisTemplate.hasKey(key);
        assertThat(isKey).isEqualTo(true);

        Thread.sleep(1000);
        isKey = redisTemplate.hasKey(key);
        assertThat(isKey).isEqualTo(true);

        Thread.sleep(2000);
        isKey = redisTemplate.hasKey(key);
        assertThat(isKey).isEqualTo(false);
    }
}