package com.cony.roomy.core.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SmsSendRequest {

    @NotEmpty(message = "휴대폰 번호를 입력해주세요")
    private String phoneNumber;
}
