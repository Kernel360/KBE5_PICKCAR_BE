package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.presentation.dto.api.KakaoReverseGeocodeResponse;
import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import com.pickcar.emulator.application.CycleQueryService;
import com.pickcar.emulator.infrastructure.dto.CycleProjection.TotalCycleData;
import com.pickcar.emulator.presentation.dto.context.PathContext;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriveHistoryService {

    @Value("${custom.driveHistory.maximum-inquiry-days}")
    private Integer maximumInquiryDays;

    @Value("${kakao.map.rest-api-key}")
    private String kakaoMapRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final CycleQueryService cycleQueryService;
    private final ReservationService reservationService;
    private final ReservationFinder reservationFinder;
    private final CycleDataExtractor cycleDataExtractor;
    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void write(DriveHistoryPayload payload) {
        Long reservationId = reservationFinder.findActiveReservation(payload);
        TotalCycleData cycleData = cycleDataExtractor.extract(payload);

        // NOTE: 외부 API 호출 비동기 Update 처리 고려 가능
        String destination = reverseGeocoding(payload.getDestLon(), payload.getDestLat());

        DriveHistory driveHistory =
                new DriveHistory(reservationId, payload.getEngineOnTime(), payload.getEngineOffTime(),
                        cycleData.getCycleIds(), cycleData.getTotalDistance() , destination);
        
        driveHistoryRepository.save(driveHistory);
    }

    private DriveHistory getById(Long id) {
        return driveHistoryRepository.findById(id)
                .orElseThrow(() -> new DriveHistoryException(DriveHistoryErrorCode.NOT_FOUND_BY_ID));
    }

    public Page<DriveHistoryListResponse> getFilteredListResponses(DriveHistoryFilterRequest filterRequest,
                                                                   Pageable pageable) {
        checkFilterRequestDate(filterRequest.getFrom(), filterRequest.getTo());
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

    private void checkFilterRequestDate(LocalDateTime from, LocalDateTime to) {
        LocalDate today = LocalDate.now();
        LocalDateTime inquiryLimitDate = today.atStartOfDay().minusDays(maximumInquiryDays);

        if (from.isAfter(to)) {
            throw new DriveHistoryException(DriveHistoryErrorCode.FROM_DATE_CANT_BE_BEFORE_TO_DATE);
        }

        if (from.isBefore(inquiryLimitDate)) {
            throw new DriveHistoryException(DriveHistoryErrorCode.MAXIMUM_INQUIRY_LIMIT_EXCEEDED);
        }
    }

    private Page<DriveHistory> getPageByFilter(DriveHistoryFilterRequest filterRequest, Pageable pageable) {
        return driveHistoryRepository.findAllFilteredListByDriverNameAndDuration(
                filterRequest.getDriverName(), filterRequest.getFrom(),
                filterRequest.getTo(), pageable
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
        ReservationContext reservationContext = reservationService.getReservationContextById(
                history.getReservationId());
        //FIXME: path를 가져올 수 있는 다른 방법 필요
        List<PathContext> pathContexts = cycleQueryService.getPathsByReservationAndHistory(
                reservationContext.reservation(), history);

        return DriveHistoryDetailResponse.of(history, reservationContext, pathContexts);
    }

    private String reverseGeocoding(Double lon, Double lat) {

        String apiUrl = "https://dapi.kakao.com/v2/local/geo/coord2regioncode?x=%f&y=%f".formatted(lon, lat);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoMapRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoReverseGeocodeResponse> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, KakaoReverseGeocodeResponse.class
            );

            KakaoReverseGeocodeResponse body = response.getBody();
            log.info("response : {} ", body.getDocuments().get(0).getRegion2depthName());
            if (body != null && body.getDocuments() != null && !body.getDocuments().isEmpty()) {
                return body.getDocuments().get(0).getRegion2depthName();
            }
        } catch (Exception e) {
            log.warn("카카오 API 호출 오류");
            return "조회_불가";
        }

        return "조회_불가";
    }
}
