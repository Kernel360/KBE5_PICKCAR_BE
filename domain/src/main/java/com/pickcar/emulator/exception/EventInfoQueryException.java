package com.pickcar.emulator.exception;

import com.pickcar.exception.GlobalException;

public class EventInfoQueryException extends GlobalException {

    public EventInfoQueryException(EventInfoQueryErrorCode errorCode) {
        super(errorCode);
    }
}
