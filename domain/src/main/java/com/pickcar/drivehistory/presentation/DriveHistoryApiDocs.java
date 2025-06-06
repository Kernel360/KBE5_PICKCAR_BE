package com.pickcar.drivehistory.presentation;

import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.presentation.dto.response.ExampleResponse;
import com.pickcar.swagger.annotation.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "DriveHistory(운행 일지) 기능 API", description = "운행일지 관련 기능 API 문서입니다.")
public interface DriveHistoryApiDocs {
}
