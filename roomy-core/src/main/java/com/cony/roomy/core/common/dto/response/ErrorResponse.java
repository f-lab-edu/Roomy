package com.cony.roomy.core.common.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends CommonResponse {

    private String code;

    private ErrorResponse(String code, String message) {
        super(message);
        this.code = code;
    }

    public static ErrorResponse failed(String code, String message) {
        return new ErrorResponse(code, message);
    }

}
