package com.pickcar.auth.exception;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.constants.GlobalStatic.HttpStatus;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;

public enum AuthErrorCode implements BaseErrorCode {

    //400(BAD_REQUEST)
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "USER_400_1", "이미 사용중인 이메일 입니다"),
    INVALID_LOGIN_INFO(HttpStatus.BAD_REQUEST, "USER_400_2", "존재하지 않는 계정이거나 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_400_3", "일치하는 회원을 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String reason;

    AuthErrorCode(HttpStatus httpStatus, String errorCode, String reason) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.reason = GlobalStatic.ERROR_PREFIX + reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public ErrorReason getErrorReason() {
        return new ErrorReason(errorCode, reason);
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError explainError = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(explainError) ? explainError.value() : this.getReason();
    }

    @Override
    public Integer getHttpStatusCode() {
        return httpStatus.getCode();
    }
}
