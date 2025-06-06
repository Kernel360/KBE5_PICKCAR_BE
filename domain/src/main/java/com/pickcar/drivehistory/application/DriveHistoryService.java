package com.pickcar.drivehistory.application;

import com.pickcar.auth.application.UserService;
import com.pickcar.auth.domain.User;
import com.pickcar.drivehistory.application.command.dto.WriteDriveHistoryCommandDto;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryAllListResponse;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    private final UserService userService;
    private final VehicleService vehicleService;
    private final ReservationService reservationService;
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

    public List<DriveHistoryAllListResponse> getAllList() {
        List<DriveHistory> histories = getAll30DaysList();

        log.info("histories : {}", histories.toString());

        List<DriveHistoryAllListResponse> responses = new ArrayList<>();

        for (DriveHistory history : histories) {
            //FIXME: reservation -> (user, vehicle)을 한번에 처리할 방법은?
            Reservation reservation = reservationService.getById(history.getReservationId());
            Vehicle vehicle = vehicleService.getById(reservation.getVehicleId());
            User driver = userService.getById(reservation.getUserId());

            DriveHistoryAllListResponse response = DriveHistoryAllListResponse.builder()
                    .historyId(history.getId())
                    .licensePlate(vehicle.getInfo().getLicensePlate())
                    .driverName(driver.getInfo().getName())
                    .drivingStartedAt(history.getDrivingStartedAt())
                    .drivingEndedAt(history.getDrivingEndedAt())
                    .totalDrivingTime(history.getTotalDrivingTime())
                    .totalDistance(history.getTotalDistance())
                    .build();

            responses.add(response);
        }

        log.info("responses : {}", responses.toString());

        return responses;
    }

    //30일간의 모든 운행일지를 가져오는 메서드 (관제사용 - 필터x)
    private List<DriveHistory> getAll30DaysList() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime ago30Days = LocalDate.now().minusDays(30).atStartOfDay();
        return driveHistoryRepository.findAllByCreatedAtBetween(ago30Days, today);
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
