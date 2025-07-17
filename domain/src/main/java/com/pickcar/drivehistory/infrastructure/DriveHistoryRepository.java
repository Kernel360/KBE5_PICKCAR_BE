package com.pickcar.drivehistory.infrastructure;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryDetailProjection;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DriveHistoryRepository extends JpaRepository<DriveHistory, Long> {
    @Query("""
            SELECT new com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection(
                dh.id,
                dh.drivingStartedAt,
                dh.drivingEndedAt,
                dh.totalDistance,
                dh.totalDrivingTime,
                dh.destination,
                r.id,
                u.info.name,
                v.info.licensePlate
            )
            FROM DriveHistory dh
            JOIN Reservation r ON dh.reservationId = r.id
            JOIN User u ON r.userId = u.id
            JOIN Vehicle v ON r.vehicleId = v.id
            WHERE (:driverName IS NULL OR :driverName = '' OR u.info.name = :driverName)
            AND dh.drivingStartedAt BETWEEN :from AND :to
            ORDER BY dh.drivingEndedAt DESC
            """)
    Page<DriveHistoryListProjection> findFilteredListProjection(String driverName, LocalDateTime from,
                                                                LocalDateTime to, Pageable pageable);

    @Query("""
            SELECT new com.pickcar.drivehistory.infrastructure.dto.DriveHistoryDetailProjection(
                dh.drivingStartedAt,
                dh.totalDrivingTime,
                dh.totalDistance,
                dh.destination,
                r.status,
                u.info.name,
                v.info.licensePlate,
                v.info.model,
                v.info.carAge,
                dh.cycleIds
            )
            FROM DriveHistory dh
            JOIN Reservation r ON dh.reservationId = r.id
            JOIN User u ON r.userId = u.id
            JOIN Vehicle v ON r.vehicleId = v.id
            WHERE dh.id = :id
            """)
    Optional<DriveHistoryDetailProjection> findDetailProjectionById(Long id);
}
