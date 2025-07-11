package com.pickcar.drivehistory.infrastructure;

import com.pickcar.drivehistory.domain.DriveHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
