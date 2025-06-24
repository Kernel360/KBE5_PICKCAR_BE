package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


//TODO: Controller를 Domain이 아닌 다른 모듈로 분리하는 것은 어떨까?
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class DriveHistoryApiController implements DriveHistoryApiDocs {

    private final DriveHistoryService driveHistoryService;

    @Override
    @PostMapping("/{offEventInfoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void write(@PathVariable Long offEventInfoId) {
        log.info("POST /api/v1/history/{offEventId} : {} ", offEventInfoId);
        driveHistoryService.write(offEventInfoId);
    }

    @Override
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Page<DriveHistoryListResponse> list(@ModelAttribute DriveHistoryFilterRequest filterRequest,
                                               @PageableDefault(size = 10, sort = "drivingStartedAt", direction = Sort.Direction.DESC)
                                               Pageable pageable) {
        log.info("GET /api/v1/history/list : {} ", filterRequest);
        return driveHistoryService.getFilteredListResponses(filterRequest, pageable);
    }

    @Override
    @GetMapping("/{historyId}/detail")
    @ResponseStatus(HttpStatus.OK)
    public DriveHistoryDetailResponse detail(@PathVariable Long historyId) {
        log.info("GET /api/v1/history/{historyId}/detail : {} ", historyId);
        return driveHistoryService.getDetailResponseById(historyId);
    }
}
