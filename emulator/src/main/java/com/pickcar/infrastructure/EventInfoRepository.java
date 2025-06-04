package com.pickcar.infrastructure;

import com.pickcar.domain.EventInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
    List<EventInfo> findByVehicleIdAndStatus(Long vehicleId, Boolean status);

    Optional<EventInfo> findTopByVehicleIdOrderByEngineOffTimeDesc(Long vehicleId);
}
