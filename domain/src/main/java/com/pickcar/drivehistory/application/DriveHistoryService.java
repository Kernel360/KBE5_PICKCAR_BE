package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.emulator.application.CycleQueryService;
import com.pickcar.emulator.application.EventInfoQueryService;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.Reservation;
import java.time.Duration;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriveHistoryService {

    private final CycleQueryService cycleQueryService;
    private final EventInfoQueryService eventInfoQueryService;
    private final ReservationService reservationService;
    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void write(Long offEventInfoId) {
        EventInfo offEventInfo = eventInfoQueryService.getById(offEventInfoId);
        Reservation reservation = reservationService.getActiveReservationByVehicleId(offEventInfo.getVehicleId());
        Double totalDistance = cycleQueryService.getTotalDistanceForHistory(offEventInfo);

        DriveHistory driveHistory = DriveHistory.builder()
                .reservationId(reservation.getId())
                .drivingStartedAt(offEventInfo.getEngineOnTime())
                .drivingEndedAt(offEventInfo.getEngineOffTime())
                .totalDistance(totalDistance)
                .totalDrivingTime(LocalTime.MIDNIGHT.plus(Duration.between(offEventInfo.getEngineOnTime(),
                        offEventInfo.getEngineOffTime())))
                .build();

        driveHistoryRepository.save(driveHistory);
    }

    public DriveHistory getById(Long id) {
        return driveHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] DriveHistory Not Found By Id " + id));
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
