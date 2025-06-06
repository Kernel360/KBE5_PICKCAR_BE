package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.application.command.dto.WriteDriveHistoryCommandDto;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.emulator.domain.Cycle;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriveHistoryService {

    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void write(WriteDriveHistoryCommandDto dto) {
        DriveHistory history = DriveHistory.builder()
                .reservationId(dto.reservationId())
                .drivingStartedAt(dto.drivingStartedAt())
                .drivingEndedAt(dto.drivingEndedAt())
                .totalDrivingTime(LocalTime.MIDNIGHT.plus(
                        Duration.between(dto.drivingStartedAt(), dto.drivingEndedAt())))
                .totalDistance(calcTotalDistance(dto.cycles()))
                .build();

        log.info("history : {}", history.toString());

        driveHistoryRepository.save(history);
    }

    public DriveHistory getById(Long id) {
        return driveHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] DriveHistory Not Found By Id " + id));
    }

    private Double calcTotalDistance(List<Cycle> cycles) {
        return cycles.stream()
                .mapToDouble(Cycle::getDistance)
                .sum();
    }

//    //FIXME: 메서드 분리 및 네이밍 수정 필요, 구성 순서도 중요
//    public void checkCondition(WriteDriveHistoryCommandDto dto) {
//        if (dto.drivingStartedAt().isAfter(LocalDateTime.now())) {
//            throw new DriveHistoryException(DriveHistoryErrorCode.START_TIME_BEFORE_NOW);
//        }
//
//        if (dto.drivingEndedAt().isAfter(LocalDateTime.now())) {
//            throw new DriveHistoryException(DriveHistoryErrorCode.END_TIME_BEFORE_NOW);
//        }
//
//        if (dto.drivingEndedAt().isBefore(dto.drivingStartedAt())) {
//            throw new DriveHistoryException(DriveHistoryErrorCode.END_TIME_BEFORE_START_TIME);
//        }
//    }
}
