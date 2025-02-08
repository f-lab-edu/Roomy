package com.cony.roomy.core.common.util;

import com.cony.roomy.core.common.config.SmsProperties;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

/**
 * CoolSMS 를 활용해 SMS 인증 비즈니스 로직을 처리하는 유틸
 */
@Component
public class SmsUtil {

    private final SmsProperties smsProperties;
    private final DefaultMessageService messageService;

    public SmsUtil(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
        this.messageService = NurigoApp.INSTANCE.initialize(smsProperties.getKey(), smsProperties.getSecret(), "https://api.coolsms.co.kr");
    }

    // 단일 메세지 발송
    public String sendSMS(String to) {
        String certCode = generateCertCode();

        Message message = new Message();
        message.setFrom(smsProperties.getFromNumber());
        message.setTo(to);
        message.setText("[Roomy]\n본인확인 인증번호 [" + certCode + "]를 화면에 입력해주세요.");

        this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return certCode;
    }
    // 6자리 인증 코드 랜덤으로 생성
    private String generateCertCode() {
        return String.valueOf((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
    }

}
