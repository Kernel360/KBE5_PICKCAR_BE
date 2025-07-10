package com.pickcar.analytics.infrastructure;

import com.pickcar.analytics.domain.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

}
