package com.pickcar.dailyreport.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DynamicInfo {
    //어제자 기준 모든 운행의 총 운행 KM 수 계산
    private Double totalMovedDistance;
}
