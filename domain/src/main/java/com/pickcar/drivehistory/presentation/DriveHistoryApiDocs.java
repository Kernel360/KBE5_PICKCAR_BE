package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryAllListResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "DriveHistory(운행 일지) 기능 API", description = "운행일지 관련 기능 API 문서입니다.")
public interface DriveHistoryApiDocs {

    @Operation(summary = "운행일지 리스트 조회", description = "관제사 입장에서 전체 운행일지 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "최근 30일간의 전체 운행일지 리스트를 불러오는데 성공하였음을 의미합니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DriveHistoryAllListResponse.class))
            })
    ResponseEntity<List<DriveHistoryAllListResponse>> list();

    @Operation(summary = "운행일지 디테일 조회", description = "운행일지 ID 기반으로 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "운행일지 ID 기반으로 상세 정보를 성공적으로 불러왔음을 의미합니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DriveHistoryDetailResponse.class))
            })
    ResponseEntity<DriveHistoryDetailResponse> detail(@PathVariable Long historyId);
}
