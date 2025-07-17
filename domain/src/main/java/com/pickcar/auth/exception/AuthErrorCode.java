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
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_400_3", "일치하는 회원을 찾을 수 없습니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "USER_400_4", "비밀번호는 6자 이상 15자 이하로 입력해 주세요."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "USER_400_5", "이름은 15자 이하로 입력해 주세요."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "USER_400_6", "휴대폰 번호는 010, 011 등으로 시작하며, 총 10~11자리여야 합니다."),
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "USER_400_7", "값을 입력해 주세요."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USER_400_8", "이메일 형식이 올바르지 않습니다(example@pickcar.com)");

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
