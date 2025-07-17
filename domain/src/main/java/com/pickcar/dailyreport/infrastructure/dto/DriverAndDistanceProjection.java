package com.pickcar.dailyreport.infrastructure.dto;

public record DriverAndDistanceProjection(
        String driverName,
        Double totalDistance
) {
}
