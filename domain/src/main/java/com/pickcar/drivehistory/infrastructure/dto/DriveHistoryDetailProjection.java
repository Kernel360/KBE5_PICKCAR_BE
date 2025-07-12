package com.pickcar.drivehistory.infrastructure.dto;

import com.pickcar.drivehistory.domain.Region1Depth;
import com.pickcar.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record DriveHistoryDetailProjection(
        LocalDateTime drivingStartedAt,
        LocalTime totalDrivingTime,
        Double totalDistance,
        Region1Depth destination,
        ReservationStatus reservationStatus,
        String driverName,
        String licensePlate,
        String model,
        String carAge,
        List<Long> cycleIds
) {
}
