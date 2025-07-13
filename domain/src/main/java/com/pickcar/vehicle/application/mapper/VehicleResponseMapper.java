package com.pickcar.vehicle.application.mapper;

import com.pickcar.vehicle.infrastructure.dto.AssignedVehiclesProjection;
import com.pickcar.vehicle.infrastructure.dto.AvailableVehicleProjection;
import com.pickcar.vehicle.presentation.dto.response.AvailableVehicleListResponse;
import com.pickcar.vehicle.presentation.dto.response.SearchAbleVehiclesResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VehicleResponseMapper {

    public List<SearchAbleVehiclesResponse> toAssignedVehiclesResponse(List<AssignedVehiclesProjection> projections) {
        return projections.stream()
                .map(SearchAbleVehiclesResponse::from)
                .toList();
    }

    public List<AvailableVehicleListResponse> toAvailableVehicleListResponse(List<AvailableVehicleProjection> projections) {
        return projections.stream()
                .map(AvailableVehicleListResponse::from)
                .toList();
    }
}
