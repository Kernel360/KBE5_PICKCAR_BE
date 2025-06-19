package com.pickcar.emulator.domain;

import com.pickcar.dto.CycleInfoConverter;
import com.pickcar.dto.CycleInfoPayload;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "cycles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cycle {

    private static final Double EARTH_RADIUS_M = 6371000.0D;        //지구 반지금 * 미터(m) 단위

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

    private LocalDateTime occurredAt;

    private Integer cycleCnt;

    private Double distance;

    @Convert(converter = CycleInfoConverter.class)
    @Column(columnDefinition = "text")
    private List<CycleInfoPayload> cycleInfos;

    public Cycle(Long vehicleId, LocalDateTime occurredAt, Integer cycleCnt, List<CycleInfoPayload> cycleInfos) {
        this.vehicleId = vehicleId;
        this.occurredAt = occurredAt;
        this.cycleCnt = cycleCnt;
        this.distance = calcCycleDistance(cycleInfos);
        this.cycleInfos = cycleInfos;
    }

    private Double calcCycleDistance(List<CycleInfoPayload> cycleInfos) {
        if (cycleInfos.size() <= 1) {
            log.info("거리 계산이 필요하지 않은 정보입니다 : {}", cycleInfos);
            return 0.0D;
        }

        return IntStream.range(0, cycleInfos.size() - 1)
                .mapToDouble(i -> calculateDistanceBetween(
                        cycleInfos.get(i), cycleInfos.get(i + 1)
                )).sum();
    }

    private Double calculateDistanceBetween(CycleInfoPayload origin, CycleInfoPayload dest) {
        double haversineComponent = calcHaversineComponent(origin, dest);
        double centralAngle = calcCentralAngle(haversineComponent);

        return EARTH_RADIUS_M * centralAngle;
    }

    //삼각함수로 구성된 거리 비율(중심각을 구하기 위한 중간 계산값)
    private Double calcHaversineComponent(CycleInfoPayload origin, CycleInfoPayload dest) {
        double originLatitudeRad = Math.toRadians(origin.getLatitude());
        double destLatitudeRad = Math.toRadians(dest.getLatitude());
        double deltaLatitude = Math.toRadians(dest.getLatitude() - origin.getLatitude());
        double deltaLongitude = Math.toRadians(dest.getLongitude() - origin.getLongitude());

        return Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(originLatitudeRad) * Math.cos(destLatitudeRad) * Math.pow(Math.sin(deltaLongitude / 2),
                2);
    }

    //angularDistanceFactor(haversineComponent) 를 이용해 아크탄젠트(atan2) 로 변환된 최종 각도(두 지점 사이의 중심각)
    private Double calcCentralAngle(Double haversineComponent) {
        return 2 * Math.atan2(Math.sqrt(haversineComponent), Math.sqrt(1 - haversineComponent));
    }
}
