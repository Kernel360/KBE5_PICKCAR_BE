package com.pickcar.drivehistory.infrastructure;

import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryDetailProjection;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DriveHistoryQueryRepository {

    private final DriveHistoryRepository driveHistoryRepository;

    public Page<DriveHistoryListProjection> findFilteredList(
            DriveHistoryFilterRequest filterRequest,
            Pageable pageable) {
        return driveHistoryRepository.findFilteredListProjection(
                filterRequest.getDriverName(),
                filterRequest.getFrom(),
                filterRequest.getTo(),
                pageable
        );
    }

    public Optional<DriveHistoryDetailProjection> findDetailById(Long historyId) {
        return driveHistoryRepository.findDetailProjectionById(historyId);
    }
}
