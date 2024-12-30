package com.cony.roomy.core.common.dto.response;

import lombok.Getter;

@Getter
public class CommonResponse {
    private String message;

    public CommonResponse(String message) {
        this.message = message;
    }
}
