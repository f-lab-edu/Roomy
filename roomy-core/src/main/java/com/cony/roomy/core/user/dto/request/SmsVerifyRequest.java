package com.cony.roomy.core.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SmsVerifyRequest {

    @NotNull(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "인증번호를 입력해주세요.")
    private String certCode;
}
