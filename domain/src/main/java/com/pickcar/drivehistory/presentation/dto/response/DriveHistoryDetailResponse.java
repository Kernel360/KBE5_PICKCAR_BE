package com.pickcar.drivehistory.presentation.dto.response;

import com.pickcar.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record DriveHistoryDetailResponse(
        String licensePlate,
        String model,
        String carAge,
        ReservationStatus reservationStatus,
        LocalDateTime drivingStartedAt,
        LocalTime totalDrivingTime,
        Double totalDistance,
        String driverName
//        Double speedAvg
//        Double lastLongitude,
//        Double lastLatitude
) {
}
