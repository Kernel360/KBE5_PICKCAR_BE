package com.pickcar.reservation.presentation.dto.response;

import com.pickcar.auth.presentation.dto.response.UnAllocatedEmployeeResponse;
import com.pickcar.vehicle.presentation.dto.response.UnAllocatedVehicleResponse;
import java.util.List;

public record ReservationPreInfoResponse(
        List<UnAllocatedEmployeeResponse> employeeResponses,
        List<UnAllocatedVehicleResponse> vehicleResponses
) {
}
