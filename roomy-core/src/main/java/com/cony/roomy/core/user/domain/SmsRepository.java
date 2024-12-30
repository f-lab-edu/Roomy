package com.cony.roomy.core.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class SmsRepository {

    private final String SMS_PREFIX = "sms:";
    private final String RATE_LIMIT_PREFIX = "sms:rate_limit:";
    private final String FAIL_COUNT_PREFIX = "sms:fail:";
    private final String SUCCESS_PREFIX = "sms:success:";

    private final long CODE_DURATION = 3 * 60; // 인증코드 유효시간: 3분
    private final long RATE_LIMIT_DURATION = 10; // 재 요청 제한 시간: 10초
    private final long FAIL_DURATION = 30 * 60; // 5회 이상 실패 시 이용제한 시간: 30분
    private final long SUCCESS_DURATION = 30 * 60; // 인증 성공 시 회원가입까지 유효기간: 30분

    // 레디스 작업을 위한 객체
    private final StringRedisTemplate stringRedisTemplate;

    // 인증번호 생성
    public void saveSmsCertCode(String phone, String code) {
        stringRedisTemplate.opsForValue()
                .set(SMS_PREFIX + phone, code, CODE_DURATION, TimeUnit.SECONDS);
    }
    // 인증번호 조회
    public String findSmsCertCode(String phone) {
        return stringRedisTemplate.opsForValue().get(SMS_PREFIX + phone);
    }
    // 인증번호 삭제: 레디스에서 해당 키 삭제
    public void deleteSmsCertCode(String phone) {
        stringRedisTemplate.delete(SMS_PREFIX + phone);
    }
    // 인증번호 존재 여부 판단
    public boolean hasSmsCertCode(String phone) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(SMS_PREFIX + phone));
    }

    // 인증번호 재 요청 유효시간 정보 생성
    public void saveRateLimit(String phone) {
        stringRedisTemplate.opsForValue()
                .set(RATE_LIMIT_PREFIX + phone, "WAIT", RATE_LIMIT_DURATION, TimeUnit.SECONDS);
    }
    // 인증번호 재 요청 유효시간 존재여부 조회
    public boolean hasRateLimit(String phone) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(RATE_LIMIT_PREFIX + phone));
    }

    // 인증실패 카운트 증가
    public void incrementFailCount(String phone) {
        Long currentFailCount = stringRedisTemplate.opsForValue().increment(FAIL_COUNT_PREFIX + phone);

        if (currentFailCount.equals(1L)) {
            stringRedisTemplate.expire(FAIL_COUNT_PREFIX + phone, FAIL_DURATION, TimeUnit.SECONDS);
        }
    }
    // 인증실패 카운트 조회
    public String findFailCount(String phone) {
        return stringRedisTemplate.opsForValue().get(FAIL_COUNT_PREFIX + phone);
    }
    // 인증실패 카운트 정보 삭제
    public void deleteFailCount(String phone) {
        stringRedisTemplate.delete(FAIL_COUNT_PREFIX + phone);
    }

    // 인증성공
    public void saveSuccessLog(String phone) {
        stringRedisTemplate.opsForValue()
                .set(SUCCESS_PREFIX + phone, "SUCCESS", SUCCESS_DURATION, TimeUnit.SECONDS);
    }
    // 인증성공 키 검증
    public boolean hasSuccessLog(String phone) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(SUCCESS_PREFIX + phone));
    }

    // 인증성공 삭제
    public void deleteSuccessLog(String phone) {
        stringRedisTemplate.delete(SUCCESS_PREFIX + phone);
    }
}
