package com.pickcar.reservation.presentation.dto.response;

import com.pickcar.reservation.presentation.dto.context.RelatedHistoryContext;
import com.pickcar.vehicle.domain.VehicleInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationDetailResponse(
        Long reservationId,
        String employeeName,
        String phoneNumber,
        VehicleInfo vehicleInfo,
        LocalDate dueDate,
        LocalDateTime rentedAt,
        List<RelatedHistoryContext> relatedHistoryContexts
) {
}
