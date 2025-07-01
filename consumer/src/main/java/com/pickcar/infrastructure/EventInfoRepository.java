package com.pickcar.infrastructure;

import com.pickcar.emulator.domain.EventInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
    Optional<EventInfo> findTopByVehicleIdOrderByEngineOffTimeDesc(Long vehicleId);
    // TODO : findTopByCarIdOrderByEngineOnTimeDesc로 만들어서 정확한 OnOff 최신 값으로 분할할까 고민중
    Optional<EventInfo> findTopByVehicleIdOrderByIdDesc(Long vehicleId);
}
