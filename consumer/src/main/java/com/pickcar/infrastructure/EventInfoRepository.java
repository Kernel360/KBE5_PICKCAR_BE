package com.pickcar.infrastructure;

import com.pickcar.emulator.domain.EventInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
    Optional<EventInfo> findTopByVehicleIdOrderByEngineOffTimeDesc(Long vehicleId);
}
