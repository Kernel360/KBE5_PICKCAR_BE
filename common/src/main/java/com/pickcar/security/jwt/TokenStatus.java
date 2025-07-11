package com.pickcar.security.jwt;


import org.springframework.http.HttpStatus;

public enum TokenStatus {
    VALID(HttpStatus.OK,"VALID","유효한 토큰입니다."),
    EXPIRED(HttpStatus.BAD_REQUEST,"EXPIRED","토큰이 만료되었습니다."),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST,"INVALID_SIGNATURE","서명이 유효하지 않습니다."),
    INVALID(HttpStatus.BAD_REQUEST,"INVALID","유효하지 않은 토큰입니다."),
    MALFORMED(HttpStatus.UNAUTHORIZED,"MALFORMED","토큰 형식이 올바르지 않습니다."),
    MISSING(HttpStatus.UNAUTHORIZED,"MISSING","토큰이 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String description;

    TokenStatus(HttpStatus httpStatus, String errorCode, String description) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.description = description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

}
