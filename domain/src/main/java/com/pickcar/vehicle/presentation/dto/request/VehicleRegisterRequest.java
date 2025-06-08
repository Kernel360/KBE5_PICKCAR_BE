package com.pickcar.vehicle.presentation.dto.request;

import com.pickcar.vehicle.domain.VehicleInfo;

public record VehicleRegisterRequest(
        VehicleInfo vehicleInfo,
        Boolean hasGps
        //TODO: 차종 (ex. 경차, 승합차, ... )
) {
}
