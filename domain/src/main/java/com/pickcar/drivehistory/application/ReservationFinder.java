package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.reservation.application.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationFinder {

    private final ReservationService reservationService;

    public Long findActiveReservation(DriveHistoryPayload payload) {
        return reservationService.getActiveReservationId(
                payload.getVehicleId(),
                payload.getUserId()
        );
    }
}
