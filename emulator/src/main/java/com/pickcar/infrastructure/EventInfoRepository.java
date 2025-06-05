package com.pickcar.infrastructure;

import com.pickcar.domain.EventInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
    Optional<EventInfo> findTopByVehicleIdOrderByEngineOffTimeDesc(Long vehicleId);
}
