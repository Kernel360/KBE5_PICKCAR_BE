package com.pickcar.dailyreport.domain;

import com.pickcar.dailyreport.converter.DestinationCountStatsConverter;
import com.pickcar.dailyreport.converter.DriverAndDistanceConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DynamicInfo {
    private Double totalMovedDistance;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = DriverAndDistanceConverter.class)
    private List<DriverAndDistanceContext> top3DriversContext;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = DestinationCountStatsConverter.class)
    private List<DestinationCountStat> destinationStats;
}
