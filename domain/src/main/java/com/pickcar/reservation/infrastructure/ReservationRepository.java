package com.pickcar.reservation.infrastructure;

import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByVehicleIdAndStatus(Long vehicleId, ReservationStatus status);
    Optional<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
    Optional<Reservation> findByVehicleIdAndUpdatedAtBetween(Long vehicleId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT v FROM Vehicle v " +
            "WHERE v.status = :vehicleStatus " +
            "AND NOT EXISTS (SELECT r FROM Reservation r " +
            "WHERE r.vehicleId = v.id AND r.status = :reservationStatus)")
    List<Vehicle> findAvailableVehicles(VehicleStatus vehicleStatus, ReservationStatus reservationStatus);

    Optional<Reservation> findByUserIdAndVehicleIdAndStatusIn(Long userId, Long vehicleId, List<ReservationStatus> statuses);
}
