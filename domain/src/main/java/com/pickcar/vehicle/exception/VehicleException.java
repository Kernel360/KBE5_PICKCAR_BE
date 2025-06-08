package com.pickcar.vehicle.exception;

import com.pickcar.exception.GlobalException;

public class VehicleException extends GlobalException {
    public VehicleException(VehicleErrorCode errorCode) {
        super(errorCode);
    }
}
