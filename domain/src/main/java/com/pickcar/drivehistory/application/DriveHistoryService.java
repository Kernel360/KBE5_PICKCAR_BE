package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryCreateRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriveHistoryService {

    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void create(DriveHistoryCreateRequest request) {
        //TODO: 유효성 검사, request 계산 시기 고려 필요
        checkCondition(request);
        DriveHistory history = DriveHistory.builder()
                .reservationId(request.reservationId())
                .drivingStartedAt(request.drivingStartedAt())
                .drivingEndedAt(request.drivingEndedAt())
                .totalDrivingTime(request.totalDrivingTime())
                .totalDistance(request.totalDistance())
                .build();

        driveHistoryRepository.save(history);
    }

    public DriveHistory getById(Long id) {
        return driveHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] DriveHistory Not Found By Id " + id));
    }

    //FIXME: 메서드 분리 및 네이밍 수정 필요, 구성 순서도 중요
    public void checkCondition(DriveHistoryCreateRequest request) {
        if (request.drivingStartedAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 운행 시작 일시는 현재 시각보다 빠를 수 없습니다.");
        }

        if (request.drivingEndedAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 운행 종료 일시는 현재 시각보다 빠를 수 없습니다.");
        }

        if (request.drivingEndedAt().isBefore(request.drivingStartedAt())) {
            throw new DriveHistoryException(DriveHistoryErrorCode.START_TIME_CANNOT_BE_AFTER_END_TIME);
        }
    }
}
