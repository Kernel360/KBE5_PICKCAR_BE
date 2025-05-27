package com.pickcar.exception;

import com.pickcar.drivehistory.exception.DriveHistoryException;
import exception.ErrorResponse;
import exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import statics.GlobalStatic;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice       //FIXME: common 모듈에 `@RestControllerAdvice`를 사용하는 클래스가 있어도 되는가? => api 모듈?
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e, HttpServletRequest request) {
//        log.error(GlobalStatic.ERROR_PREFIX + "ErrorCode : {}, URI {}, Message : {}", e.getErrorCode(),
//                request.getRequestURI(), e.getReason()); //FIXME: 도메인 커스텀 예외별 다른 네이밍 관리 방식 필요

        log.error(GlobalStatic.ERROR_PREFIX + "GlobalException => ErrorCode : {}, URI {}, Message :",
                e.getErrorCode(), request.getRequestURI(), e.getReason());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getReason());
        return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getErrorReason().getStatus()))
                .body(errorResponse);
    }

    @ExceptionHandler(DriveHistoryException.class)
    public ErrorResponse handleDriveHistoryException(DriveHistoryException e, HttpServletRequest request) {
        log.error(GlobalStatic.ERROR_PREFIX + "DriveHistoryException => ErrorCode : {}, URI {}, Message :",
                e.getErrorCode(), request.getRequestURI(), e.getReason());
        return new ErrorResponse(e.getErrorCode(), e.getReason());
    }
}
