package com.pickcar.drivehistory.application;

import com.pickcar.application.CycleInfoService;
import com.pickcar.application.EventInfoService;
import com.pickcar.auth.application.UserService;
import com.pickcar.auth.domain.User;
import com.pickcar.domain.CycleInfo;
import com.pickcar.domain.EventInfo;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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
    private final CycleInfoService cycleInfoService;
    private final EventInfoService eventInfoService;
    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void write(Long reservationId) {
        Reservation reservation = reservationService.getById(reservationId);
        User user = userService.getById(reservation.getUserId());
        Vehicle vehicle = vehicleService.getById(reservation.getVehicleId());
        Double totalDistance = 0D;

        EventInfo offEventInfo = eventInfoService.getLatestOffEventInfoByVehicleId(vehicle.getId());

        List<CycleInfo> cycleInfoList = cycleInfoService.getCycleInfosByOffEventInfo(offEventInfo);

        for (CycleInfo c : cycleInfoList) {
            Map<String, Object> cycleInfoMap = c.getCycleInfos();

            log.info("cycleInfoMap : {}", cycleInfoMap.toString());

            for (int i = 0; i < c.getCycleCnt(); i++) {
                Map<String, Object> o = (Map<String, Object>) cycleInfoMap.get(String.valueOf(i));
                Object rawDistance = o.get("total_distance");

                Double currentDistance = null;

                if (rawDistance instanceof Number) {
                    currentDistance = ((Number) rawDistance).doubleValue();
                } else {
                    log.warn("total_distance is not a number: {}", rawDistance);
                    continue; // 또는 예외 처리
                }

                log.info("currentDistance : {}", currentDistance);
                totalDistance += currentDistance;
            }
        }

        DriveHistory history = DriveHistory.builder()
                .drivingStartedAt(offEventInfo.getEngineOnTime())
                .drivingEndedAt(offEventInfo.getEngineOffTime())
                .totalDrivingTime(LocalTime.MIDNIGHT.plus(
                        Duration.between(offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime())))
                .totalDistance(totalDistance)
                .build();

        log.info("history : {}", history.toString());

        driveHistoryRepository.save(history);
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
