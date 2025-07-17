package com.pickcar.dailyreport.infrastructure.dto;

import java.time.LocalDate;

public record MovedDistanceHistoryProjection(
        LocalDate reportDate,
        Double totalMovedDistance
) {
}
