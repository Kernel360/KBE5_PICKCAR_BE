package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import com.pickcar.emulator.application.CycleQueryService;
import com.pickcar.emulator.application.EventInfoQueryService;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import java.time.LocalDateTime;
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
        EventInfo offEventInfo = eventInfoQueryService.getOffEventById(offEventInfoId);
        Reservation reservation = reservationService.getActiveReservationForDriveHistory(offEventInfo.getVehicleId());
        Double totalDistance = cycleQueryService.getTotalDistanceForHistory(offEventInfo);

        DriveHistory driveHistory = new DriveHistory(reservation.getId(), offEventInfo, totalDistance);

        driveHistoryRepository.save(driveHistory);
    }

    public DriveHistory getById(Long id) {
        return driveHistoryRepository.findById(id)
                .orElseThrow(() -> new DriveHistoryException(DriveHistoryErrorCode.NOT_FOUND_BY_ID));
    }

    public List<DriveHistoryListResponse> getFilteredListResponses(DriveHistoryFilterRequest filterRequest) {
        List<DriveHistoryListResponse> responses = new ArrayList<>();
        List<DriveHistory> histories = getFilteredList(filterRequest);

        for (DriveHistory history : histories) {
            // FIXME: N+1 여지 있음 -> histories 기반 List 조회 필요 가능성 있음
            ReservationContext context = reservationService.getReservationContextById(history.getReservationId());

            log.info("context : {} ", context);
            DriveHistoryListResponse response = DriveHistoryListResponse.of(history, context);
            responses.add(response);
        }
        return responses;
    }

    private List<DriveHistory> getFilteredList(DriveHistoryFilterRequest filterRequest) {
        LocalDateTime from = filterRequest.from();
        LocalDateTime to = filterRequest.to();

        return driveHistoryRepository.findAllFilteredListByDriverNameAndDuration(filterRequest.driverName(), from, to);
    }

    public DriveHistoryDetailResponse getDetailResponseById(Long historyId) {
        DriveHistory history = getById(historyId);
        ReservationContext context = reservationService.getReservationContextById(history.getReservationId());
        //FIXME: path를 가져올 수 있는 다른 방법 필요
        //Cycle cycle = cycleQueryService.getByVehicleId(context.reservedVehicleInfo().getId());

        return DriveHistoryDetailResponse.of(history, context);
    }
}
