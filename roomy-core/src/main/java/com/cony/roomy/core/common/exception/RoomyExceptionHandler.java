package com.cony.roomy.core.common.exception;

import com.cony.roomy.core.common.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class RoomyExceptionHandler {

    @ExceptionHandler(RoomyException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(RoomyException e) {

        e.getLogger().accept("Error Code: " + e.getErrorType().getCode());
        e.getLogger().accept("Parameter: " + e.getParameter().toString());
        e.getLogger().accept("Inner Message: " + e.getErrorType().getInnerMessage());

        return ResponseEntity.status(e.getErrorType().getHttpCode()).body(
                ErrorResponse.failed(e.getErrorType().getCode(), e.getErrorType().getOuterMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = ErrorType.INVALID.getOuterMessage();
        BindingResult bindingResult = e.getBindingResult();
        if(bindingResult.hasErrors() && bindingResult.hasFieldErrors() && Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() != null) {
            message = bindingResult.getFieldError().getDefaultMessage();
        }
        return ResponseEntity.status(e.getStatusCode()).body(
                ErrorResponse.failed(ErrorType.INVALID.getCode(), message)
        );
    }
}
