package com.pickcar.auth.exception;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.constants.GlobalStatic.HttpStatus;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.swagger.annotation.ExplainError;

import java.lang.reflect.Field;
import java.util.Objects;

public enum TokenErrorCode implements BaseErrorCode {

    //400(BAD_REQUEST)

    //500(INTERNAL_SERVER_ERROR)
    REFRESH_TOKEN_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER_500_1", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String reason;

    TokenErrorCode(HttpStatus httpStatus, String errorCode, String reason) {
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
