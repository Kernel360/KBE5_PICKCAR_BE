package com.pickcar.vehicle.application;

import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.vehicle.application.mapper.VehicleResponseMapper;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.exception.VehicleErrorCode;
import com.pickcar.vehicle.exception.VehicleException;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import com.pickcar.vehicle.infrastructure.dto.AssignedVehiclesProjection;
import com.pickcar.vehicle.presentation.dto.request.ChangeVehicleStatusRequest;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import com.pickcar.vehicle.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.vehicle.presentation.dto.response.UnAllocatedVehicleResponse;
import com.pickcar.vehicle.presentation.dto.response.VehicleListResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleResponseMapper responseMapper;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public void register(VehicleRegisterRequest request) {
        //FIXME: 분리 필요, 케이스 추가
        hasLicensePlateAlready(request.vehicleInfo().getLicensePlate());

        Vehicle vehicle = new Vehicle(request.vehicleInfo(), request.hasGps());
        vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleException(VehicleErrorCode.NOT_FOUND_BY_ID));
    }

    private void hasLicensePlateAlready(String licensePlate) {
        if (vehicleRepository.findByInfo_LicensePlate(licensePlate).isPresent()) {
            throw new VehicleException(VehicleErrorCode.LICENSE_PLATE_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public List<VehicleListResponse> getAllList() {
        List<VehicleListResponse> responses = new ArrayList<>();

        //FIXME: findAll이 아닌 뭔가 조건이 있어야 함 (계약중인 같은)
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

        if (vehicle.getStatus().equals(request.vehicleStatus())) {
            throw new VehicleException(VehicleErrorCode.ALREADY_SET_UP_STATUS);
        }

        vehicle.changeStatus(request.vehicleStatus());
        //FIXME: 차량의 상태 <-> 예약의 상태 사용하는 구간 / 정의 / 예시 똑바로 설정
    }

    public void processRented(Long vehicleId) {
        Vehicle vehicle = getById(vehicleId);
        if (!vehicle.tryMarkAsRented()) {
            throw new VehicleException(VehicleErrorCode.ALREADY_RENTED_OR_RETURNED);
        }
    }

    public void processReturned(Long vehicleId) {
        Vehicle vehicle = getById(vehicleId);
        if (!vehicle.tryMarkAsReturned()) {
            throw new VehicleException(VehicleErrorCode.ALREADY_RENTED_OR_RETURNED);
        }
    }

    public List<SearchAbleVehiclesResponse> getAssignedVehicles() {
        List<AssignedVehiclesProjection> projections = vehicleRepository.findAssignedVehicles(VehicleStatus.OPERABLE);

        return responseMapper.toAssignedVehiclesResponse(projections);
    }
}
