package com.pickcar.drivehistory.application;

import com.pickcar.auth.application.UserService;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.vehicle.application.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriveHistoryService {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final ReservationService reservationService;
    private final DriveHistoryRepository driveHistoryRepository;

//    @Transactional
//    public void write(Long reservationId) {
//
//        //TODO: 주기정보 기반으로 시동 OFF시 운행일지 생성
//
//    }

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
