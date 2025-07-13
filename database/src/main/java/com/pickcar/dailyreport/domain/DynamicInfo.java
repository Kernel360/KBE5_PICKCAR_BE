package com.pickcar.dailyreport.domain;

import com.pickcar.dailyreport.converter.DestinationCountStatsConverter;
import com.pickcar.dailyreport.converter.DriverAndDistanceConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DynamicInfo {
    private Double totalMovedDistance;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = DriverAndDistanceConverter.class)
    private List<DriverAndDistanceContext> top3Drivers;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = DestinationCountStatsConverter.class)
    private List<DestinationCountStat> destinationStats;
}
