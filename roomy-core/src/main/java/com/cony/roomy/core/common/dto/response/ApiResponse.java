package com.cony.roomy.core.common.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> extends CommonResponse {

    private int code;
    private T data;

    private ApiResponse(int code, String message, T data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

    public static <T> ApiResponse<T> okOnlyMessage(String message) {
        return of(HttpStatus.OK.value(), message, null);
    }

    public static <T> ApiResponse<T> created(String message) {
        return of(HttpStatus.CREATED.value(), message, null);
    }

    public static <T> ApiResponse<T> noContent() {
        return of(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.name(), null);
    }
}
