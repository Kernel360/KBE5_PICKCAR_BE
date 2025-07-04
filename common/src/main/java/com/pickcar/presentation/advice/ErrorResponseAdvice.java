package com.pickcar.presentation.advice;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.exception.ErrorReason;
import com.pickcar.exception.GlobalException;
import com.pickcar.presentation.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorResponseAdvice {

    @ExceptionHandler(GlobalException.class)     //NOTE: 특정 도메인에 대한 핸들링으로도 변경 가능
    public ResponseEntity<ErrorResponse> handleException(GlobalException e, HttpServletRequest request) {

        StackTraceElement origin = e.getStackTrace()[0];

        log.warn(" ErrorCode : {}, URI {}, Message : {}, Thrown at: ({}:{})",
                e.getErrorReason().errorCode(), request.getRequestURI(), e.getErrorReason().reason(),
                origin.getFileName(),
                origin.getLineNumber());

        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatusCode(), e.getErrorReason());

        return ResponseEntity.status(e.getHttpStatusCode()).body(errorResponse);
    }
}
