package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryAllListResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Void> write(@PathVariable Long offEventInfoId) {
        driveHistoryService.write(offEventInfoId);
        return ResponseEntity.noContent().build();
    }

    //관제사용 전체 리스트 조회
    //FIXME: api 경로를 search로 바꾸고, 관제사와 고객사는 같은 api 사용, 필터링을 통해 제공 내용 변경
    @Override
    @GetMapping("/list")
    public ResponseEntity<List<DriveHistoryAllListResponse>> list() {
        List<DriveHistoryAllListResponse> responses = driveHistoryService.getAllList();
        return ResponseEntity.ok().body(responses);
    }

    @Override
    @GetMapping("/{historyId}/detail")
    public ResponseEntity<DriveHistoryDetailResponse> detail(@PathVariable Long historyId) {
        log.info("Requesting detail for historyId: {}", historyId);
        DriveHistoryDetailResponse response = driveHistoryService.getDetailResponseById(historyId);
        return ResponseEntity.ok().body(response);
    }
}
