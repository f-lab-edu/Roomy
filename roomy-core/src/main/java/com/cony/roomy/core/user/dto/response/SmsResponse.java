package com.cony.roomy.core.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsResponse {
    private boolean success;
    private String message;

    public static SmsResponse of(boolean success, String message) {
        return SmsResponse.builder()
                .success(success)
                .message(message)
                .build();
    }
}
