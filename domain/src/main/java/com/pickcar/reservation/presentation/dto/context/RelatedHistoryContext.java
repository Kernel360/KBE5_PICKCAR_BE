package com.pickcar.reservation.presentation.dto.context;

import com.pickcar.reservation.infrastructure.dto.ReservationRelatedProjection;
import java.time.LocalDate;
import java.util.List;

public record RelatedHistoryContext(
        Long driveHistoryId,
        LocalDate drivingDate
) {
    public static List<RelatedHistoryContext> toContextList(List<ReservationRelatedProjection> projections) {
        if (projections == null) {
            return List.of();
        }

        return projections.stream()
                .map(projection -> new RelatedHistoryContext(
                        projection.driveHistoryId(),
                        projection.drivingEndedAt().toLocalDate()
                ))
                .toList();
    }
}
