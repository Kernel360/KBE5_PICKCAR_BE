package com.pickcar.dailyreport.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class StaticInfo {

    private Long totalVehicleCount;

    private Long reservedVehicleCount;

    private Long notOperableVehicleCount;

    private Long expectedReturnCount;

    private Long delayedCount;

}
