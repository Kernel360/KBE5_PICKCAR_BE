package com.pickcar.infrastructure;

import com.pickcar.emulator.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
    List<Cycle> findAllByVehicleIdAndOccurredAtBetween(Long vehicleId, LocalDateTime occurredAtStart,
                                                       LocalDateTime occurredAtEnd);
}
