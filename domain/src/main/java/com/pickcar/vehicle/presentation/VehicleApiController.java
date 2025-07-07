package com.pickcar.vehicle.presentation;

import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.presentation.dto.request.ChangeVehicleStatusRequest;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import com.pickcar.vehicle.presentation.dto.response.VehicleListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
public class VehicleApiController implements VehicleApiDocs{

    private final VehicleService vehicleService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@RequestBody VehicleRegisterRequest vehicleRegisterRequest) {
        vehicleService.register(vehicleRegisterRequest);
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleListResponse> allList() {
        return vehicleService.getAllList();
    }

    @Override
    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@RequestBody ChangeVehicleStatusRequest request) {
        vehicleService.changeStatus(request);
    }

    @GetMapping("/allocation/{userId}")
    public Long findAllocation(@PathVariable Long userId) {
        return vehicleService.getIdByUserIdFromReservation(userId);
    }
}
