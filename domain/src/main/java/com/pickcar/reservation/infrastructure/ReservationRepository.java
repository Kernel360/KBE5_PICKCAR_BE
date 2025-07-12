package com.pickcar.reservation.infrastructure;

import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Reservation> findAllByDueDate(LocalDate dueDate);

    @Query("SELECT DISTINCT r.userId FROM Reservation r WHERE r.status IN :statuses")
    List<Long> findUserIdsByStatusIn(@Param("statuses") List<ReservationStatus> statuses);

    @Query("SELECT DISTINCT r.vehicleId FROM Reservation r WHERE r.status IN :statuses")
    List<Long> findVehicleIdsByStatusIn(@Param("statuses") List<ReservationStatus> statuses);

    Long countByStatusIn(List<ReservationStatus> reservationstatuses);
    Long countByStatus(ReservationStatus reservationStatus);
    Long countByStatusAndDueDateBetween(ReservationStatus reservationStatus, LocalDate from, LocalDate to);

    @Query("SELECT v FROM Vehicle v " +
            "WHERE v.status = :vehicleStatus " +
            "AND EXISTS (SELECT r FROM Reservation r " +
            "WHERE r.vehicleId = v.id AND r.status = :reservationStatus)")
    List<Vehicle> findAssignedVehicles(VehicleStatus vehicleStatus, ReservationStatus reservationStatus);
}
