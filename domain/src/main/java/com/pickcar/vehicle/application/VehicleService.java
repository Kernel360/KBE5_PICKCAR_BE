package com.pickcar.vehicle.application;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import com.pickcar.vehicle.presentation.dto.request.ChangeVehicleStatusRequest;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import com.pickcar.vehicle.presentation.dto.response.VehicleListResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Transactional
    public void register(VehicleRegisterRequest request) {
        //FIXME: 분리 필요, 케이스 추가
        if (hasLicensePlateAlready(request.vehicleInfo().getLicensePlate())) {
            throw new IllegalArgumentException("[ERROR] 동일한 번호판을 사용하는 자동차가 이미 존재합니다.");
        }

        Vehicle vehicle = new Vehicle(request.vehicleInfo(), request.hasGps());
        vehicleRepository.save(vehicle);
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Car Not Found By Id " + id));
    }

    private boolean hasLicensePlateAlready(String licensePlate) {
        return vehicleRepository.findByInfo_LicensePlate(licensePlate).isPresent();
    }

    public List<VehicleListResponse> getAllList() {
        List<VehicleListResponse> responses = new ArrayList<>();

        for (Vehicle v : vehicleRepository.findAll()) {
            VehicleInfo info = v.getInfo();
            VehicleListResponse response = new VehicleListResponse(v.getId(), info.getLicensePlate(), info.getModel(),
                    info.getColor(), v.getStatus(), "빌린 회사명", LocalDate.now());
            //FIXME: 빌린 회사 명, 빌린 시각 수정 필요, status도 rental관련 status 여야함
            
            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public void changeStatus(ChangeVehicleStatusRequest request) {
        Vehicle vehicle = getById(request.vehicleId());

        if(vehicle.getStatus().equals(request.vehicleStatus())) {
            throw new IllegalArgumentException("[ERROR] 동일한 상태로는 변경할 수 없습니다");
        }

        vehicle.changeStatus(request.vehicleStatus());
        //FIXME: 차량의 상태 <-> 예약의 상태 사용하는 구간 / 정의 / 예시 똑바로 설정
    }
}
