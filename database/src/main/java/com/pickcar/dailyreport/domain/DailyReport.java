package com.pickcar.dailyreport.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="daily_reports")
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDate reportDate;

    @Embedded
    private StaticInfo staticInfo;

    public DailyReport(LocalDate reportDate, StaticInfo staticInfo) {
        this.reportDate = reportDate;
        this.staticInfo = staticInfo;
    }
}
