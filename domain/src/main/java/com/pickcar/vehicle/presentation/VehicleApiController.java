package com.pickcar.vehicle.presentation;

import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
public class VehicleApiController {

    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(VehicleRegisterRequest vehicleRegisterRequest) {
        log.info("Registering vehicle: {}", vehicleRegisterRequest);
        vehicleService.register(vehicleRegisterRequest);
    }
}
