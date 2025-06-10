package com.pickcar.vehicle.application;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Transactional
    public void create(VehicleInfo info) {

//        //FIXME: 책임 분리 필요
        if (hasLicensePlateAlready(info.getLicensePlate())) {
            throw new IllegalArgumentException("[ERROR] 동일한 번호판을 사용하는 자동차가 이미 존재합니다.");
        }

        //FIXME : 전체적으로 하드코딩 금지
        Vehicle vehicle = Vehicle.builder()
                .info(info)
                .hasGps(true)
                .isRented(false)
                .status(VehicleStatus.OPERABLE)
                .build();

        vehicleRepository.save(vehicle);
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Car Not Found By Id " + id));
    }

    private boolean hasLicensePlateAlready(String licensePlate) {
        return vehicleRepository.findByInfo_LicensePlate(licensePlate).isPresent();
    }
}
