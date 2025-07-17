package com.pickcar.dailyreport.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverAndDistanceContext {
    private String driverName;
    private Double totalDistance;
}
