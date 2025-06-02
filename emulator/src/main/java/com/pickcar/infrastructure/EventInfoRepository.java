package com.pickcar.infrastructure;

import com.pickcar.domain.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
}
