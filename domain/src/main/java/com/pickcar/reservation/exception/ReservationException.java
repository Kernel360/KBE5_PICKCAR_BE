package com.pickcar.reservation.exception;

import com.pickcar.exception.GlobalException;

public class ReservationException extends GlobalException {
    public ReservationException(ReservationErrorCode errorCode) {
        super(errorCode);
    }
}
