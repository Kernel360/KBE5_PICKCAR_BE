package com.pickcar.emulator.infrastructure;

import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.infrastructure.dto.CycleProjection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CycleQueryRepository extends JpaRepository<Cycle, Long> {


    @Query("SELECT new com.pickcar.emulator.presentation.dto.CycleIdAndDistance(c.id, c.distance) " +
            "FROM Cycle c WHERE c.vehicleId = :vehicleId " +
            "AND c.occurredAt BETWEEN :start AND :end")
    List<CycleProjection> findAllByVehicleIdAndOccurredAtBetween(Long vehicleId, LocalDateTime start,
                                                                 LocalDateTime end);

    @Query(value = "SELECT * FROM cycles c WHERE c.vehicle_id = :vehicleId LIMIT 1", nativeQuery = true)
    Optional<Cycle> findByVehicleId(Long vehicleId);
}
