package com.pickcar.emulator.infrastructure;

import com.pickcar.emulator.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CycleQueryRepository extends JpaRepository<Cycle, Long> {
    List<Cycle> findAllByVehicleIdAndOccurredAtBetween(Long vehicleId, LocalDateTime occurredAtStart,
                                                       LocalDateTime occurredAtEnd);

    @Query(value = "SELECT * FROM cycles c WHERE c.vehicle_id = :vehicleId LIMIT 1", nativeQuery = true)
    Optional<Cycle> findByVehicleId(Long vehicleId);
}
