package com.pickcar.infrastructure;

import com.pickcar.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
    List<Cycle> findAllByVehicleIdAndOccurredAtBetween(Long vehicleId, LocalDateTime occurredAtStart,
                                                       LocalDateTime occurredAtEnd);

//    @Query(value = """
//        SELECT
//            JSON_UNQUOTE(JSON_EXTRACT(j.value, '$.latitude')) AS latitude,
//            JSON_UNQUOTE(JSON_EXTRACT(j.value, '$.longitude')) AS longitude
//        FROM cycle_info ci,
//             JSON_TABLE(
//                 ci.cycle_infos,
//                 '$.*' COLUMNS (
//                     value JSON PATH '$'
//                 )
//             ) AS j
//        WHERE ci.vehicle_id = :vehicleId
//          AND ci.occurred_at BETWEEN :start AND :end
//        """, nativeQuery = true)
//    List<LatLngDto> findLatLngByVehicleIdAndOccurredAtBetween(
//            @Param("vehicleId") Long vehicleId,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
}
