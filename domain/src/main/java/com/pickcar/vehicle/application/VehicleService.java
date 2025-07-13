package com.pickcar.vehicle.application;

import com.pickcar.vehicle.application.mapper.VehicleResponseMapper;
import com.pickcar.vehicle.application.validator.VehicleValidator;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.exception.VehicleErrorCode;
import com.pickcar.vehicle.exception.VehicleException;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import com.pickcar.vehicle.infrastructure.dto.AssignedVehiclesProjection;
import com.pickcar.vehicle.infrastructure.dto.AvailableVehicleProjection;
import com.pickcar.vehicle.presentation.dto.request.ChangeVehicleStatusRequest;
import com.pickcar.vehicle.presentation.dto.request.VehicleRegisterRequest;
import com.pickcar.vehicle.presentation.dto.response.AvailableVehicleListResponse;
import com.pickcar.vehicle.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.vehicle.presentation.dto.response.VehicleListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleValidator validator;
    private final VehicleResponseMapper responseMapper;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public void register(VehicleRegisterRequest request) {
        validator.validateRegisterRequest(request);
        Vehicle vehicle = new Vehicle(request.vehicleInfo(), request.hasGps());
        vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleException(VehicleErrorCode.NOT_FOUND_BY_ID));
    }

    @Transactional(readOnly = true)
    public List<VehicleListResponse> getAllList() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return responseMapper.toListResponses(vehicles);
    }

    @Transactional
    public void changeStatus(ChangeVehicleStatusRequest request) {
        Vehicle targetVehicle = getById(request.vehicleId());
        validator.validateChangeStatusRequest(request, targetVehicle);
        targetVehicle.changeStatus(request.vehicleStatus());
    }

    @Transactional
    public void processRented(Long vehicleId) {
        Vehicle vehicle = getById(vehicleId);
        if (!vehicle.tryMarkAsRented()) {
            throw new VehicleException(VehicleErrorCode.ALREADY_RENTED_OR_RETURNED);
        }
    }

    @Transactional
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

    public List<AvailableVehicleListResponse> getAvailableVehicles() {
        List<AvailableVehicleProjection> projections = vehicleRepository.findAvailableVehicles(VehicleStatus.OPERABLE);
        return responseMapper.toAvailableVehicleListResponse(projections);
    }

    public Long getAllCount() {
        return vehicleRepository.count();
    }
}
