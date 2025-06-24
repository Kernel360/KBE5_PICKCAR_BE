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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriveHistoryService {

    @Value("${custom.driveHistory.maximumInquiryDays}")
    private Integer maximumInquiryDays;

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

    private DriveHistory getById(Long id) {
        return driveHistoryRepository.findById(id)
                .orElseThrow(() -> new DriveHistoryException(DriveHistoryErrorCode.NOT_FOUND_BY_ID));
    }

    public Page<DriveHistoryListResponse> getFilteredListResponses(DriveHistoryFilterRequest filterRequest,
                                                                   Pageable pageable) {
        checkFilterRequest(filterRequest);
        Page<DriveHistory> filteredHistoryPage = getPageByFilter(filterRequest, pageable);
        List<Long> reservationIds = getRelatedReservationIdsByPage(filteredHistoryPage);
        Map<Long, ReservationContext> contextMap = reservationService.getContextMapByIds(reservationIds);

        List<DriveHistoryListResponse> responses = filteredHistoryPage.getContent()
                .stream()
                .map(history -> {
                    ReservationContext context = contextMap.get(history.getReservationId());
                    return DriveHistoryListResponse.of(history, context);
                })
                .toList();

        return new PageImpl<>(responses, pageable, filteredHistoryPage.getTotalElements());
    }

    private void checkFilterRequest(DriveHistoryFilterRequest filterRequest) {
        checkFilterRequestDate(filterRequest.from(), filterRequest.to());
        //TODO: 검사 추가
    }

    private void checkFilterRequestDate(LocalDateTime from, LocalDateTime to) {
        LocalDate today = LocalDate.now();
        LocalDateTime inquiryLimitDate = today.atStartOfDay().minusDays(maximumInquiryDays);

        if(from.isAfter(to)) {
            throw new DriveHistoryException(DriveHistoryErrorCode.FROM_DATE_CANT_BE_BEFORE_TO_DATE);
        }

        if (from.isBefore(inquiryLimitDate)) {
            throw new DriveHistoryException(DriveHistoryErrorCode.MAXIMUM_INQUIRY_LIMIT_EXCEEDED);
        }
    }

    private Page<DriveHistory> getPageByFilter(DriveHistoryFilterRequest filterRequest, Pageable pageable) {
        return driveHistoryRepository.findAllFilteredListByDriverNameAndDuration(
                filterRequest.driverName(), filterRequest.from(),
                filterRequest.to(), pageable
        );
    }

    private List<Long> getRelatedReservationIdsByPage(Page<DriveHistory> driveHistoryPage) {
        return driveHistoryPage.getContent()
                .stream()
                .map(DriveHistory::getReservationId)
                .toList();
    }

    public DriveHistoryDetailResponse getDetailResponseById(Long historyId) {
        DriveHistory history = getById(historyId);
        ReservationContext context = reservationService.getReservationContextById(history.getReservationId());
        //FIXME: path를 가져올 수 있는 다른 방법 필요
        //Cycle cycle = cycleQueryService.getByVehicleId(context.reservedVehicleInfo().getId());

        return DriveHistoryDetailResponse.of(history, context);
    }
}
