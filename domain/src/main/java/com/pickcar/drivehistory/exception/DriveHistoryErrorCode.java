package com.pickcar.drivehistory.exception;

import exception.ErrorCode;
import exception.ErrorReason;
import java.lang.reflect.Field;
import org.springframework.http.HttpStatus;

public enum DriveHistoryErrorCode implements ErrorCode {

    //운행 종료 일시는 운행 시작 일시보다 빠를 수 없습니다
    START_TIME_CANNOT_BE_AFTER_END_TIME(HttpStatus.BAD_REQUEST, "DH_400_1", "운행 종료 일시는 운행 시작 일시보다 빠를 수 없습니다");

    private HttpStatus status;
    private String code;
    private String msg;

    DriveHistoryErrorCode(HttpStatus status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public ErrorReason getErrorReason() {
        return new ErrorReason(status.value(), code, msg);
    }
}
