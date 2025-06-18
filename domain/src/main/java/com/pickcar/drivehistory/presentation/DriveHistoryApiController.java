package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        driveHistoryService.write(offEventInfoId);
    }

    @Override
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<DriveHistoryListResponse> list(@RequestBody(required = false) DriveHistoryFilterRequest filterRequest) {
        return driveHistoryService.getListResponses(filterRequest);
    }

    @Override
    @GetMapping("/{historyId}/detail")
    public ResponseEntity<DriveHistoryDetailResponse> detail(@PathVariable Long historyId) {
        log.info("Requesting detail for historyId: {}", historyId);
        DriveHistoryDetailResponse response = driveHistoryService.getDetailResponseById(historyId);
        return ResponseEntity.ok().body(response);
    }
}
