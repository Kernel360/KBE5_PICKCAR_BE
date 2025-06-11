package com.pickcar.vehicle.presentation;

import com.pickcar.swagger.annotation.ApiErrorCodeExample;
import com.pickcar.vehicle.exception.VehicleErrorCode;
import com.pickcar.vehicle.presentation.dto.request.ChangeVehicleStatusRequest;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import com.pickcar.vehicle.presentation.dto.response.VehicleListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Vehicle(자동차 관리) 기능 API", description = "자동차 관련 기능 API 문서입니다.")
public interface VehicleApiDocs {

    @Operation(summary = "자동차 등록 API", description = "새로운 자동차 정보를 등록합니다.")
    @ApiResponse(responseCode = "204", description = "자동차 등록에 성공하였습니다.")
    @ApiErrorCodeExample(VehicleErrorCode.class)
    void register(VehicleRegisterRequest request);

    @Operation(summary = "자동차 전체 조회 API", description = "전체 자동차 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "전체 자동차 목록을 불러왔습니다.")
    List<VehicleListResponse> allList();

    @Operation(summary = "자동차 상태 변경 API", description = "자동차의 상태를 변경합니다.")
    @ApiResponse(responseCode = "204", description = "자동차 등록에 성공하였습니다.")
    @ApiErrorCodeExample(VehicleErrorCode.class)
    void changeStatus(ChangeVehicleStatusRequest request);
}
