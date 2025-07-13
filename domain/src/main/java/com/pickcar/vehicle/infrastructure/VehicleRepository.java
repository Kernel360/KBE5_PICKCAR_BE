package com.pickcar.vehicle.infrastructure;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.infrastructure.dto.AssignedVehiclesProjection;
import com.pickcar.vehicle.infrastructure.dto.AvailableVehicleProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByInfo_LicensePlate(String licensePlate);

    @Query("""
            SELECT new com.pickcar.vehicle.infrastructure.dto.AssignedVehiclesProjection(
            v.id,
            v.info,
            v.status
            )
            FROM Vehicle v
            WHERE v.isRented = true
            AND v.status = :status
            ORDER BY v.info.licensePlate
            """)
    List<AssignedVehiclesProjection> findAssignedVehicles(VehicleStatus status);

    @Query("""
            SELECT new com.pickcar.vehicle.infrastructure.dto.AvailableVehicleProjection(
                v.id,
                v.info
            )
            FROM Vehicle v
            WHERE v.isRented = false
            AND v.status = :status
            ORDER BY v.info.licensePlate
            """)
    List<AvailableVehicleProjection> findAvailableVehicles(VehicleStatus status);
}
