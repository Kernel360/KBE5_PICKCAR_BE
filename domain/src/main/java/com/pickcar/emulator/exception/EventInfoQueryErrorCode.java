package com.pickcar.emulator.exception;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.constants.GlobalStatic.HttpStatus;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;

public enum EventInfoQueryErrorCode implements BaseErrorCode {

    //400(BAD_REQUEST) ERROR
    EVENT_STATUS_NOT_OFF(HttpStatus.BAD_REQUEST, "EIQ_400_1", "이벤트의 상태가 OFF가 아닙니다."),

    //404(NOT_FOUND) ERROR
    NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "EIQ_404_1", "해당 ID를 가진 이벤트 정보를 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String reason;

    EventInfoQueryErrorCode(HttpStatus httpStatus, String errorCode, String reason) {
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
