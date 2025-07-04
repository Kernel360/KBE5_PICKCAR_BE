package com.pickcar.security.jwt;

public enum TokenStatus {
    VALID("유효한 토큰입니다."),
    EXPIRED("토큰이 만료되었습니다."),
    INVALID_SIGNATURE("서명이 유효하지 않습니다."),
    MALFORMED("토큰 형식이 올바르지 않습니다."),
    INVALID("유효하지 않은 토큰입니다.");

    private final String description;

    TokenStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
