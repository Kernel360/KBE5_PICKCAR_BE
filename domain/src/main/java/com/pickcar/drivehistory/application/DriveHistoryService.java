package com.pickcar.drivehistory.application;

import com.pickcar.drivehistory.application.mapper.DriveHistoryResponseMapper;
import com.pickcar.drivehistory.application.query.CycleInfoQuery;
import com.pickcar.drivehistory.application.query.ReservationQuery;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.infrastructure.DriveHistoryQueryRepository;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryDetailProjection;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection;
import com.pickcar.drivehistory.presentation.dto.api.KakaoReverseGeocodeResponse;
import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import com.pickcar.emulator.infrastructure.dto.CycleProjection.TotalCycleData;
import com.pickcar.emulator.presentation.dto.context.PathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

    private final ReservationQuery reservationQuery;
    private final CycleInfoQuery cycleInfoQuery;
    private final DriveHistoryResponseMapper responseMapper;

    private final DriveHistoryQueryRepository queryRepository;
    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void write(DriveHistoryPayload payload) {
        Long reservationId = reservationQuery.findActiveReservation(payload);
        TotalCycleData cycleData = cycleInfoQuery.findCycleInfo(payload);

        // NOTE: 외부 API 호출 비동기 Update 처리 고려 가능
        String destination = reverseGeocoding(payload.getDestLon(), payload.getDestLat());

        DriveHistory driveHistory =
                new DriveHistory(reservationId, payload.getEngineOnTime(), payload.getEngineOffTime(),
                        cycleData.getCycleIds(), cycleData.getTotalDistance() , destination);
        
        driveHistoryRepository.save(driveHistory);
    }

    public Page<DriveHistoryListResponse> getFilteredListResponses(DriveHistoryFilterRequest filterRequest,
                                                                   Pageable pageable) {
        checkFilterRequestDate(filterRequest.getFrom(), filterRequest.getTo());

        Page<DriveHistoryListProjection> projectionPage = queryRepository.findFilteredList(filterRequest, pageable);

        return responseMapper.toResponsePage(projectionPage);
    }

    public DriveHistoryDetailResponse getDetailResponseById(Long historyId) {
        DriveHistoryDetailProjection detailProjection = driveHistoryRepository
                .findDetailProjectionById(historyId)
                .orElseThrow(() -> new DriveHistoryException(DriveHistoryErrorCode.NOT_FOUND_BY_ID));

        List<PathContext> pathContexts = cycleInfoQuery.findPathsByCycleIds(detailProjection.cycleIds());

        return responseMapper.toResponseDetail(detailProjection, pathContexts);
    }

    //NOTE: Validator 클래스 분리 가능
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
