package com.pickcar.emulator.domain;

import com.pickcar.emulator.domain.converter.CycleInfoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

    private LocalDateTime occurredAt;

    private Integer cycleCnt;

    private Double distance;

    @Convert(converter = CycleInfoConverter.class)
    @Column(columnDefinition = "text")
    private List<CycleInfo> cycleInfos;

    private static final Double EARTH = 6371000.0D;

    public Cycle(Long vehicleId, LocalDateTime occurredAt, Integer cycleCnt, List<CycleInfo> cycleInfos) {
        this.vehicleId = vehicleId;
        this.occurredAt = occurredAt;
        this.cycleCnt = cycleCnt;
        this.distance = calcCycleDistance(cycleInfos);
        this.cycleInfos = cycleInfos;
    }

    private Double calcCycleDistance(List<CycleInfo> cycleInfos) {
        double totalDistance = 0.0D;

        if (cycleInfos.size() <= 1) {
            log.info("거리 계산이 필요하지 않은 정보입니다 : {}", cycleInfos);
        }

        for (int i = 0; i < cycleInfos.size() - 1; i++) {
            Double originLatitude = cycleInfos.get(i).getLatitude();
            Double originLongitude = cycleInfos.get(i).getLongitude();
            Double destLatitude = cycleInfos.get(i + 1).getLatitude();
            Double destLongitude = cycleInfos.get(i + 1).getLongitude();

            Double lat1Rad = Math.toRadians(originLatitude);
            Double lat2Rad = Math.toRadians(destLatitude);
            Double deltaLatitude = Math.toRadians(destLatitude - originLatitude);
            Double deltaLongitude = Math.toRadians(destLongitude - originLongitude);

            Double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                    + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                    * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);

            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            Double currentDistance = 6371000.0D * c; //지구 반지름 : 약 6371 * 1000(km)

            totalDistance += currentDistance;
        }

        return totalDistance;
    }
}
