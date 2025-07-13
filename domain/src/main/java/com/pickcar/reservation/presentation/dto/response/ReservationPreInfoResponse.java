package com.pickcar.reservation.presentation.dto.response;

import com.pickcar.reservation.infrastructure.dto.EmployeeReservationProjection;
import java.util.List;

public record ReservationPreInfoResponse(
        List<EmployeeReservationProjection> employeePreInfos
) {
}
