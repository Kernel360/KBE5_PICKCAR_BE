package com.pickcar.analytics.domain;

import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Analytics extends BaseEntity {
    @Embedded
    private StaticInfo staticInfo;

    private LocalDate reportDate;
}
