package com.pickcar.auth.exception;

import com.pickcar.exception.ErrorReason;
import com.pickcar.presentation.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.pickcar.auth.presentation")
public class AuthExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleAuthValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String message = "입력값이 유효하지 않습니다.";
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }

        AuthErrorCode errorCode = AuthErrorCode.INVALID_LOGIN_INFO;  // 대표 에러코드

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getHttpStatusCode(),
                errorCode.getErrorReason().getCode(),
                message
        );

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(errorResponse);
    }
}
