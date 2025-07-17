package com.pickcar.reservation.infrastructure.dto;

import java.time.LocalDateTime;

public record ReservationRelatedProjection(
        Long driveHistoryId,
        LocalDateTime drivingEndedAt
) {
}
