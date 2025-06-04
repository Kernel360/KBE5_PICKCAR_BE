package com.pickcar.drivehistory.domain;

import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
}
