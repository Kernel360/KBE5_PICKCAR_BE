package com.pickcar.drivehistory.domain;

import com.pickcar.emulator.infrastructure.CycleIdsConverter;
import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

    @Enumerated(EnumType.STRING)
    private Region1Depth destination;

    public DriveHistory(Long reservationId, LocalDateTime engineOnTime, LocalDateTime engineOffTime,
                        List<Long> cycleIds, Double totalDistance, String destination) {
        this.reservationId = reservationId;
        this.drivingStartedAt = engineOnTime;
        this.drivingEndedAt = engineOffTime;
        this.totalDistance = totalDistance;
        this.totalDrivingTime = calcTotalDrivingTime(engineOnTime, engineOffTime);
        this.cycleIds = cycleIds;
        this.destination = Region1Depth.valueOf(destination);
    }

    private LocalTime calcTotalDrivingTime(LocalDateTime engineOnTime, LocalDateTime engineOffTime) {
        return LocalTime.MIDNIGHT.plus(Duration.between(engineOnTime, engineOffTime));
    }
}
