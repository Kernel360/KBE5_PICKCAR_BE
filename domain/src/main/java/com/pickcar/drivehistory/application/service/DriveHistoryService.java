package com.pickcar.drivehistory.application.service;

import com.pickcar.drivehistory.application.mapper.DriveHistoryResponseMapper;
import com.pickcar.drivehistory.application.validator.DriveHistoryValidator;
import com.pickcar.dto.command.DriveHistoryWriteCommand;
import com.pickcar.emulator.application.CycleQueryService;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryDetailProjection;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection;
import com.pickcar.drivehistory.presentation.dto.api.KakaoReverseGeocodeResponse;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import com.pickcar.emulator.infrastructure.dto.CycleProjection.TotalCycleData;
import com.pickcar.emulator.infrastructure.dto.PathContext;
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

    // Util
    @Value("${kakao.map.rest-api-key}")
    private String kakaoMapRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DriveHistoryResponseMapper responseMapper;
    private final DriveHistoryValidator validator;

    // Service
    private final ReservationService reservationService;
    private final CycleQueryService cycleQueryService;

    // Repository
    private final DriveHistoryRepository driveHistoryRepository;

    @Transactional
    public void write(DriveHistoryWriteCommand command) {
        validator.validatePayload(command);
        Long reservationId = reservationService.getActiveReservationId(command);
        TotalCycleData cycleData = cycleQueryService.getCyclesBetweenOnOffTime(command);
        String destination = reverseGeocoding(command.destLon(), command.destLat()); // NOTE: 외부 API 호출 및 비동기 Update 처리 고려 가능

        DriveHistory driveHistory = new DriveHistory(
                reservationId,
                command.engineOnTime(),
                command.engineOffTime(),
                cycleData.getCycleIds(),
                cycleData.getTotalDistance(),
                destination
        );

        driveHistoryRepository.save(driveHistory);
    }

    public Page<DriveHistoryListResponse> getFilteredListResponses(DriveHistoryFilterRequest filterRequest,
                                                                   Pageable pageable) {
        validator.checkFilterRequestDate(filterRequest.getFrom(), filterRequest.getTo());

        Page<DriveHistoryListProjection> projectionPage = driveHistoryRepository.findFilteredListProjection(
                filterRequest.getDriverName(),
                filterRequest.getFrom(),
                filterRequest.getTo(),
                pageable
        );;

        return responseMapper.toResponsePage(projectionPage);
    }

    public DriveHistoryDetailResponse getDetailResponseById(Long historyId) {
        DriveHistoryDetailProjection detailProjection = driveHistoryRepository
                .findDetailProjectionById(historyId)
                .orElseThrow(() -> new DriveHistoryException(DriveHistoryErrorCode.NOT_FOUND_BY_ID));

        List<PathContext> pathContexts = cycleQueryService.extractPathContexts(detailProjection.cycleIds());

        return responseMapper.toResponseDetail(detailProjection, pathContexts);
    }

    //NOTE: 별도 공통 외부 Service로 분리 가능 (ex. LocationService)
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
