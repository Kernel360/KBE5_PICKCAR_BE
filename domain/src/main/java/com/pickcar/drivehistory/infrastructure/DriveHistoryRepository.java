package com.pickcar.drivehistory.infrastructure;

import com.pickcar.drivehistory.domain.DriveHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveHistoryRepository extends JpaRepository<DriveHistory, Long> {
    List<DriveHistory> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
