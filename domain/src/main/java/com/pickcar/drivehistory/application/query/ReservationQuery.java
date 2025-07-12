package com.pickcar.drivehistory.application.query;

import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.reservation.application.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationQuery {

    private final ReservationService reservationService;

    public Long findActiveReservation(DriveHistoryPayload payload) {
        return reservationService.getActiveReservationId(
                payload.getVehicleId(),
                payload.getUserId()
        );
    }
}
