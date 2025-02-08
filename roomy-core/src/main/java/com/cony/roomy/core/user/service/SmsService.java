package com.cony.roomy.core.user.service;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.user.dto.request.SmsSendRequest;
import com.cony.roomy.core.user.dto.request.SmsVerifyRequest;
import com.cony.roomy.core.user.domain.SmsRepository;
import com.cony.roomy.core.common.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {

    private final SmsUtil smsUtil;
    private final SmsRepository smsRepository;

    @Transactional
    public void sendSMS(SmsSendRequest smsSendRequest) {
        String phoneNumber = smsSendRequest.getPhoneNumber();
        // 10초안에 같은 휴대폰으로 인증번호를 보낸적이 있으면 에러 리턴
        if(smsRepository.hasRateLimit(phoneNumber)) {
            throw new RoomyException(ErrorType.USER_EXCEED_RATE_LIMIT, Map.of(), log::warn);
        }
        // 인증 시도 횟수 5회 초과 시 30분 제한
        String failCount = smsRepository.findFailCount(phoneNumber);
        if(failCount != null && Integer.parseInt(failCount) >= 5) {
            throw new RoomyException(ErrorType.USER_EXCEED_FAIL_COUNT, Map.of(), log::warn);
        }
        // SMS 인증번호 전송
        String certCode = smsUtil.sendSMS(phoneNumber);
        // 전송한 인증 코드와 rate limit 정보 저장
        smsRepository.saveRateLimit(phoneNumber);
        // 10초이후부터 인증코드 유효시간 사이에 인증 코드 재발송 시 기존 코드 삭제
        if(smsRepository.hasSmsCertCode(phoneNumber)) {
            smsRepository.deleteSmsCertCode(phoneNumber);
        }
        smsRepository.saveSmsCertCode(phoneNumber, certCode);
    }

    @Transactional
    public void verifyCode(SmsVerifyRequest smsVerifyRequest) {
        String phoneNumber = smsVerifyRequest.getPhoneNumber();
        // 인증 시도 실패 횟수가 있으면서 5회 초과 시 30분 제한
        String failCount = smsRepository.findFailCount(phoneNumber);
        if(failCount != null && Integer.parseInt(failCount) >= 5) {
            throw new RoomyException(ErrorType.USER_EXCEED_FAIL_COUNT, Map.of(), log::warn);
        }
        // 휴대폰 번호가 redis에 존재하면서 저장된 인증 코드와 입력된 인증 코드가 일치하는지 확인
        if(!smsRepository.hasSmsCertCode(phoneNumber)) {
            throw new RoomyException(ErrorType.USER_CODE_NOT_FOUND, Map.of(), log::warn);
        }
        String repositorySmsCertCode = smsRepository.findSmsCertCode(phoneNumber);
        // 인증 코드 비교 실패 시 카운트
        if(!smsVerifyRequest.getCertCode().equals(repositorySmsCertCode)) {
            smsRepository.incrementFailCount(phoneNumber);
            throw new RoomyException(ErrorType.USER_CODE_INVALID, Map.of(), log::warn);
        }

        // 일치하므로 해당 사용자의 휴대폰 인증 코드를 삭제하고 true 반환
        smsRepository.deleteSmsCertCode(phoneNumber);
        // 실패한 경우가 있으면 카운트 정보도 함께 삭제
        smsRepository.deleteFailCount(phoneNumber);
        // 인증성공 내역 저장
        smsRepository.saveSuccessLog(phoneNumber);
    }
}
