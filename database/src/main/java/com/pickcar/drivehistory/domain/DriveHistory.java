package com.pickcar.drivehistory.domain;

import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public DriveHistory(Long reservationId, EventInfo offEventInfo, Double totalDistance) {
        this.reservationId = reservationId;
        this.drivingStartedAt = offEventInfo.getEngineOnTime();
        this.drivingEndedAt = offEventInfo.getEngineOffTime();
        this.totalDistance = totalDistance != null ? totalDistance / 1000 : 0.0;        //FIXME: 임시 위치
        this.totalDrivingTime = calcTotalDrivingTime(offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());
    }

    private LocalTime calcTotalDrivingTime(LocalDateTime engineOnTime, LocalDateTime engineOffTime) {
        return LocalTime.MIDNIGHT.plus(Duration.between(engineOnTime, engineOffTime));
    }
}
