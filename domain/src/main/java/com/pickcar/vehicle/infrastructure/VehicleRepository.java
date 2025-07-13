package com.pickcar.vehicle.infrastructure;

import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByInfo_LicensePlate(String licensePlate);

    // TODO : 단수만 처리하도록 고려
    @Query("SELECT r.vehicleId FROM Reservation r WHERE r.userId = :id AND r.status = 'RESERVED' ")
    Long findVehicleIdByUserId(@Param("id") Long userId);

    List<Vehicle> findAllByIdNotInAndStatus(List<Long> allocatedVehicleIds, VehicleStatus status);

    @Query("SELECT v FROM Vehicle v " +
            "WHERE v.status = :vehicleStatus " +
            "AND EXISTS (SELECT r FROM Reservation r " +
            "WHERE r.vehicleId = v.id AND r.status IN :reservationStatuses)")
    List<Vehicle> findAssignedVehicles(VehicleStatus vehicleStatus, List<ReservationStatus> reservationStatuses);
}
