package com.pickcar.emulator.infrastructure.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record CycleProjection (
        Long id,
        Double distance
){
    private static List<Long> extractIds(List<CycleProjection> projections) {
        return projections.stream()
                .map(CycleProjection::id)
                .collect(Collectors.toList());
    }

    private static double calcTotalDistance(List<CycleProjection> projections) {
        return projections.stream()
                .mapToDouble(CycleProjection::distance)
                .sum();
    }

    @Getter
    @AllArgsConstructor
    public static class TotalCycleData {
        List<Long> cycleIds;
        Double totalDistance;

        public TotalCycleData(List<CycleProjection> cycleProjections) {
            this.cycleIds = extractIds(cycleProjections);
            this.totalDistance = calcTotalDistance(cycleProjections);
        }

        public static TotalCycleData empty() {
            return new TotalCycleData(new ArrayList<>(), 0.0D);
        }
    }
}
