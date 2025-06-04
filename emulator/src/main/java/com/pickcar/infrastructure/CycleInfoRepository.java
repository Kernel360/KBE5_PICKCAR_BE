package com.pickcar.infrastructure;

import com.pickcar.domain.CycleInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleInfoRepository extends JpaRepository<CycleInfo, Long> {
    List<CycleInfo> findByVehicleIdAndOccurredAtBetween(Long vehicleId, LocalDateTime occurredAtStart, LocalDateTime occurredAtEnd);
}
