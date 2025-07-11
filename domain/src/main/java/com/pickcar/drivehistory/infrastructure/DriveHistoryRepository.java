package com.pickcar.drivehistory.infrastructure;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.presentation.dto.context.Top3DriverDistanceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriveHistoryRepository extends JpaRepository<DriveHistory, Long> {
    @Query("""
            SELECT dh
            FROM DriveHistory dh
            JOIN Reservation r ON dh.reservationId = r.id
            JOIN User u ON r.userId = u.id
            WHERE (:driverName IS NULL OR :driverName = '' OR u.info.name = :driverName)
                        AND dh.drivingStartedAt between :from AND :to
            ORDER BY dh.drivingStartedAt ASC
            """)
    Page<DriveHistory> findAllFilteredListByDriverNameAndDuration(String driverName, LocalDateTime from,
                                                                  LocalDateTime to, Pageable pageable);

    List<DriveHistory> findAllByDrivingEndedAtBetween(LocalDateTime from, LocalDateTime to);


    @Query(value = """
             SELECT u.name as driverName, SUM(dh.total_distance) as totalDistance
             FROM drive_histories as dh
             JOIN reservations as r ON dh.reservation_id = r.id
             JOIN users as u ON r.user_id = u.id
             WHERE DATE(dh.driving_ended_at) = :yesterday
             GROUP BY r.user_id, u.name
             ORDER BY SUM(dh.total_distance) DESC
             LIMIT 3
            """, nativeQuery = true)
    List<Top3DriverDistanceContext> findTop3EmployeeNameAndTotalDistance(@Param("yesterday") LocalDate yesterday);
}
