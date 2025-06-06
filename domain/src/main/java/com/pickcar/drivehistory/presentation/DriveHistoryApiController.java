package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryAllListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//TODO: Controller를 Domain이 아닌 다른 모듈로 분리하는 것은 어떨까?
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class DriveHistoryApiController implements DriveHistoryApiDocs {

    private final DriveHistoryService driveHistoryService;

    //관제사용 전체 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<DriveHistoryAllListResponse>> allList() {
        List<DriveHistoryAllListResponse> responses = driveHistoryService.getAllList();

        return ResponseEntity.ok().body(responses);
    }
}
