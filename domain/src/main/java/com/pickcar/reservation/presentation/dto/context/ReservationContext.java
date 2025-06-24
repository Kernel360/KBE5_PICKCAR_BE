package com.pickcar.reservation.presentation.dto.context;

import com.pickcar.auth.domain.UserInfo;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.vehicle.domain.VehicleInfo;

public record ReservationContext(
        Reservation reservation,
        UserInfo reservedUserInfo,
        VehicleInfo reservedVehicleInfo
) {
}
