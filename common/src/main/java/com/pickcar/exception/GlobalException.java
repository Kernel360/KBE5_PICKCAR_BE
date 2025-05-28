package com.pickcar.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GlobalException extends RuntimeException {

    private BaseErrorCode errorCode;

    public GlobalException(BaseErrorCode errorCode) {
        super(errorCode.getErrorReason().reason());
        this.errorCode = errorCode;
    }

    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }

    public Integer getHttpStatusCode() {
        return this.errorCode.getHttpStatusCode();
    }
}
