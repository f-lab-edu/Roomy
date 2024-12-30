package com.cony.roomy.api.domain.user;

import com.cony.roomy.core.common.dto.response.ApiResponse;
import com.cony.roomy.core.user.dto.request.SmsSendRequest;
import com.cony.roomy.core.user.dto.request.SmsVerifyRequest;
import com.cony.roomy.core.user.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SmsController {

    private final SmsService smsService;

    // 문자 인증번호 전송
    @PostMapping("/sms/send")
    public ApiResponse<Void> sendSMS(@RequestBody @Valid SmsSendRequest smsSendRequest) {
        smsService.sendSMS(smsSendRequest);
        return ApiResponse.okOnlyMessage("인증번호 전송 완료");
    }

    // 문자 인증
    @PostMapping("/sms/verify")
    public ApiResponse<Void> verifyCode(@RequestBody @Valid SmsVerifyRequest smsVerifyRequest) {
        smsService.verifyCode(smsVerifyRequest);
        return ApiResponse.okOnlyMessage("인증번호 검증 완료");
    }
}
