package com.pickcar.drivehistory.domain;

import com.pickcar.dto.CycleInfoConverter;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.emulator.infrastructure.CycleIdsConverter;
import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "drive_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DriveHistory extends BaseEntity {

    private Long reservationId;

    @Column(nullable = false)
    private LocalDateTime drivingStartedAt;

    private LocalDateTime drivingEndedAt;

    private Double totalDistance;

    private LocalTime totalDrivingTime;

    @Column(columnDefinition = "text")
    @Convert(converter = CycleIdsConverter.class)
    List<Long> cycleIds;

    public DriveHistory(Long reservationId, LocalDateTime engineOnTime, LocalDateTime engineOffTime,
                        List<Long> cycleIds, List<Double> distances) {
        this.reservationId = reservationId;
        this.drivingStartedAt = engineOnTime;
        this.drivingEndedAt = engineOffTime;
        this.totalDistance = calcTotalDistance(distances);
        this.totalDrivingTime = calcTotalDrivingTime(engineOnTime, engineOffTime);
        this.cycleIds = cycleIds;
    }

    private LocalTime calcTotalDrivingTime(LocalDateTime engineOnTime, LocalDateTime engineOffTime) {
        return LocalTime.MIDNIGHT.plus(Duration.between(engineOnTime, engineOffTime));
    }

    private Double calcTotalDistance(List<Double> distances) {
        return distances.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
