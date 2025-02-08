package com.cony.roomy.core.common.exception;

import lombok.Getter;

import java.util.Map;
import java.util.function.Consumer;

@Getter
public class RoomyException extends RuntimeException {

    private final ErrorType errorType;
    private final Map<String, Object> parameter;
    private final Consumer<String> logger; // 컨슈머를 활용해 로그를 accept 하도록 함.

    public RoomyException(ErrorType errorType, Map<String, Object> parameter, Consumer<String> logger) {
        this.errorType = errorType;
        this.parameter = parameter;
        this.logger = logger;
    }
}
