package com.pickcar.drivehistory.infrastructure;

import com.pickcar.drivehistory.domain.DriveHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveHistoryQueryRepository extends JpaRepository<DriveHistory, Long> {


}
