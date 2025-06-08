package com.pickcar.vehicle.exception;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.constants.GlobalStatic.HttpStatus;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;

public enum VehicleErrorCode implements BaseErrorCode {

    //400(BAD_REQUEST)
    LICENSE_PLATE_DUPLICATED(HttpStatus.BAD_REQUEST, "VEHICLE_400_1", "동일한 번호판을 사용하는 자동차가 이미 존재합니다."),
    NOT_FOUND_BY_ID(HttpStatus.BAD_REQUEST, "VEHICLE_400_2", "해당 ID를 가진 자동차를 찾을 수 없습니다."),
    ALREADY_SET_UP_STATUS(HttpStatus.BAD_REQUEST, "VEHICLE_400_3", "동일한 상태로는 변경할 수 없습니다");

    private HttpStatus httpStatus;
    private String errorCode;
    private String reason;

    VehicleErrorCode(HttpStatus httpStatus, String errorCode, String reason) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.reason = GlobalStatic.ERROR_PREFIX + reason;       //FIXME: prefix를 여기서 붙여도 될까?
    }

    public HttpStatus getStatus() {
        return httpStatus;
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
