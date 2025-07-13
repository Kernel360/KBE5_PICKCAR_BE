package com.pickcar.emulator.infrastructure;

import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.infrastructure.dto.CycleInfoProjection;
import com.pickcar.emulator.infrastructure.dto.CycleProjection;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CycleQueryRepository extends JpaRepository<Cycle, Long> {

    @Query("""
            SELECT new com.pickcar.emulator.infrastructure.dto.CycleProjection(
                c.id,
                c.distance
            )
            FROM Cycle c WHERE c.vehicleId = :vehicleId
            AND c.occurredAt BETWEEN :start AND :end
            """)
    List<CycleProjection> findAllByVehicleIdAndOccurredAtBetween(Long vehicleId, LocalDateTime start,
                                                                 LocalDateTime end);

    @Query("""
            SELECT new com.pickcar.emulator.infrastructure.dto.CycleInfoProjection(
                c.cycleInfos
            )
            FROM Cycle c
            WHERE c.id IN :cycleIds
            ORDER BY c.occurredAt ASC
            """)
    List<CycleInfoProjection> findCycleInfoProjections(List<Long> cycleIds);
}
