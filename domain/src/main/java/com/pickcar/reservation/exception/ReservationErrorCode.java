package com.pickcar.reservation.exception;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.constants.GlobalStatic.HttpStatus;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;

public enum ReservationErrorCode implements BaseErrorCode {

    //400(BAD_REQUEST)
    EMPLOYEE_ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "RS_400_1", "해당 사원은 이미 할당된 차량이 있습니다."),
    VEHICLE_ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "RS_400_2", "이미 예약된 차량입니다."),

    //404(NOT_FOUND)
    NOT_FOUND_ACTIVE_RESERVATION_BY_VEHICLE_ID(HttpStatus.NOT_FOUND, "RS_404_1", "해당 자동차의 예약 기록을 찾을 수 없습니다."),
    NOT_FOUND_LATEST_UPDATED_RESERVATION(HttpStatus.NOT_FOUND, "RS_404_2", "해당 자동차의 최근 수정된 예약 기록을 찾을 수 없습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String reason;

    ReservationErrorCode(HttpStatus httpStatus, String errorCode, String reason) {
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
