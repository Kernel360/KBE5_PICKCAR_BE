package com.pickcar.emulator.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CycleIdAndDistance {
    private Long id;
    private Double distance;

    public static List<Long> toCycleIds(List<CycleIdAndDistance> cycles) {
        return cycles.stream().map(CycleIdAndDistance::getId).collect(Collectors.toList());
    }

    public static List<Double> toDistances(List<CycleIdAndDistance> cycles) {
        return cycles.stream().map(CycleIdAndDistance::getDistance).collect(Collectors.toList());
    }
}
