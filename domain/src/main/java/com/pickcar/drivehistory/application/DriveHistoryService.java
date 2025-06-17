package com.pickcar.drivehistory.application;

import com.pickcar.auth.application.UserService;
import com.pickcar.auth.domain.User;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryAllListResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.emulator.application.CycleQueryService;
import com.pickcar.emulator.application.EventInfoQueryService;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
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
                .orElseThrow(() -> new DriveHistoryException(DriveHistoryErrorCode.NOT_FOUND_BY_ID));
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

    public List<DriveHistoryAllListResponse> getAllList() {
        List<DriveHistoryAllListResponse> responses = new ArrayList<>();
        List<DriveHistory> histories = getAll30DaysList();

        for (DriveHistory history : histories) {
            ReservationContext context = reservationService.getReservationContextById(history.getReservationId());

            DriveHistoryAllListResponse response = DriveHistoryAllListResponse.builder()
                    .historyId(history.getId())
                    .licensePlate(context.reservedVehicleInfo().getLicensePlate())
                    .driverName(context.reservedUserInfo().getName())
                    .drivingStartedAt(history.getDrivingStartedAt())
                    .drivingEndedAt(history.getDrivingEndedAt())
                    .totalDrivingTime(history.getTotalDrivingTime())
                    .totalDistance(history.getTotalDistance())
                    .build();

            responses.add(response);
        }
        return responses;
    }

    //30일간의 모든 운행일지를 가져오는 메서드 (관제사용 - 필터x)
    private List<DriveHistory> getAll30DaysList() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime ago30Days = LocalDate.now().minusDays(30).atStartOfDay();
        return driveHistoryRepository.findAllByCreatedAtBetween(ago30Days, today);
    }

    public DriveHistoryDetailResponse getDetailResponseById(Long historyId) {
        //FIXME: 자꾸 거쳐 거쳐 조회하지 말고, 쿼리문을 쓰던지, 메서드를 만들던지 변경 필요
        DriveHistory history = getById(historyId);
        ReservationContext context = reservationService.getReservationContextById(history.getReservationId());
//        Cycle cycle = cycleQueryService.getByVehicleId(context.reservedVehicleInfo().getId());      //FIXME: path를 가져올 수 있는 다른 방법 필요

        return DriveHistoryDetailResponse.builder()
                .licensePlate(context.reservedVehicleInfo().getLicensePlate())
                .model(context.reservedVehicleInfo().getModel())
                .carAge(context.reservedVehicleInfo().getCarAge())
                .reservationStatus(context.reservation().getStatus())
                .drivingStartedAt(history.getDrivingStartedAt())
                .totalDrivingTime(history.getTotalDrivingTime())
                .totalDistance(history.getTotalDistance())
                .driverName(context.reservedUserInfo().getName())
//                .path(cycle.getCycleInfos())
                .build();
    }
}
