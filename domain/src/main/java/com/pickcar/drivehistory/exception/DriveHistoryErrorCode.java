package com.pickcar.drivehistory.exception;

import com.pickcar.constants.GlobalStatic;
import com.pickcar.constants.GlobalStatic.HttpStatus;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;


public enum DriveHistoryErrorCode implements BaseErrorCode {

    //400(BAD_REQUEST)
    END_TIME_BEFORE_START_TIME(HttpStatus.BAD_REQUEST, "DH_400_1", "운행 종료 일시는 운행 시작 일시보다 빠를 수 없습니다."),
    END_TIME_BEFORE_NOW(HttpStatus.BAD_REQUEST, "DH_400_2", "운행 종료 일시는 현재 시각보다 빠를 수 없습니다."),
    START_TIME_BEFORE_NOW(HttpStatus.BAD_REQUEST, "DH_400_3", "운행 시작 일시는 현재 시각보다 빠를 수 없습니다."),
    NOT_FOUND_BY_ID(HttpStatus.BAD_REQUEST, "DH_400_4", "해당 ID를 가진 운행일지를 찾을 수 없습니다."),

    //403(FORBIDDEN)
    TEST_403_ERROR(HttpStatus.FORBIDDEN, "DH_403_1", "테스트용 403 에러");

    private HttpStatus httpStatus;
    private String errorCode;
    private String reason;

    DriveHistoryErrorCode(HttpStatus httpStatus, String errorCode, String reason) {
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
