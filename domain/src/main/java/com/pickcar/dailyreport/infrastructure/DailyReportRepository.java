package com.pickcar.dailyreport.infrastructure;

import com.pickcar.dailyreport.domain.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

}
