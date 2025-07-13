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

    private Integer totalVehicleCount;

    private Integer reservedVehicleCount;

    private Integer notOperableVehicleCount;

    private Integer expectedReturnCount;

    private Integer delayedCount;

}
