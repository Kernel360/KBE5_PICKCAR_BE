package com.pickcar.vehicle.application.validator;

import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.exception.VehicleErrorCode;
import com.pickcar.vehicle.exception.VehicleException;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleValidator {

    private final VehicleRepository vehicleRepository;

    public void validateRegisterRequest(VehicleRegisterRequest request) {
        VehicleInfo requestedInfo = request.vehicleInfo();
        hasLicensePlateAlready(request.vehicleInfo().getLicensePlate());
        validateStringField(List.of(requestedInfo.getLicensePlate(), requestedInfo.getModel(),
                requestedInfo.getColor(), requestedInfo.getCarAge(), requestedInfo.getBrandName()));
    }

    private void hasLicensePlateAlready(String licensePlate) {
        if (vehicleRepository.existsByInfo_LicensePlate(licensePlate)) {
            throw new VehicleException(VehicleErrorCode.LICENSE_PLATE_DUPLICATED);
        }
    }

    private void validateStringField(List<String> stringFields) {
        stringFields.forEach(field -> {
            if (field == null || field.isBlank() || field.length() > 10) {
                throw new VehicleException(VehicleErrorCode.REGISTER_REQUEST_FORMAT_INVALID);
            }
        });
    }
}
