package com.pickcar.reservation.infrastructure;

import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByVehicleIdAndStatus(Long vehicleId, ReservationStatus status);

    Optional<Reservation> findByVehicleIdAndUpdatedAtBetween(Long vehicleId, LocalDateTime from, LocalDateTime to);
}
