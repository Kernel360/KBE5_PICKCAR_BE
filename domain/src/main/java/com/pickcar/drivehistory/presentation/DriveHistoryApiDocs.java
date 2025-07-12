package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryFilterRequest;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "DriveHistory(운행 일지) 기능 API", description = "운행일지 관련 기능 API 문서입니다.")
public interface DriveHistoryApiDocs {

    @Operation(summary = "운행일지 리스트 조회", description = "관제사 입장에서 전체 운행일지 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "최근 30일간의 전체 운행일지 리스트를 불러오는데 성공하였음을 의미합니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DriveHistoryListResponse.class))
            })
    Page<DriveHistoryListResponse> list(@ModelAttribute DriveHistoryFilterRequest filterRequest,
                                               @PageableDefault(size = 10, sort = "drivingStartedAt", direction = Sort.Direction.DESC)
                                               Pageable pageable);

    @Operation(summary = "운행일지 디테일 조회", description = "운행일지 ID 기반으로 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "운행일지 ID 기반으로 상세 정보를 성공적으로 불러왔음을 의미합니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DriveHistoryDetailResponse.class))
            })
    DriveHistoryDetailResponse detail(@PathVariable Long historyId);
}
