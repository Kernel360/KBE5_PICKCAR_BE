package com.pickcar.reservation.infrastructure;

import com.pickcar.auth.domain.UserRole;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.infrastructure.dto.AllocatedReservationInfoProjection;
import com.pickcar.reservation.infrastructure.dto.EmployeeReservationProjection;
import com.pickcar.reservation.infrastructure.dto.ReservationDetailProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Boolean existsByVehicleIdAndStatusIn(Long vehicleId, List<ReservationStatus> statuses);

    Boolean existsByUserIdAndStatusIn(Long userId, List<ReservationStatus> statuses);

    Optional<Reservation> findByUserIdAndVehicleIdAndStatusIn(Long userId, Long vehicleId,
                                                              List<ReservationStatus> statuses);

    List<Reservation> findAllByDueDate(LocalDate dueDate);

    @Query("""
            SELECT new com.pickcar.reservation.infrastructure.dto.EmployeeReservationProjection(
                u.id,
                u.info.name,
                u.info.email,
                u.role,
                CASE WHEN r.id IS NOT NULL THEN true ELSE false END,
                v.info.licensePlate
            )
            FROM User u
            LEFT JOIN Reservation r ON u.id = r.userId AND r.status IN :statuses
            LEFT JOIN Vehicle v ON r.vehicleId = v.id
            WHERE u.role = :role
            """)
    List<EmployeeReservationProjection> findEmployeesWithReservationPreInfo(UserRole role,
                                                                            List<ReservationStatus> statuses);

    @Query("SELECT r.id FROM Reservation r WHERE r.vehicleId = :vehicleId " +
            "AND r.userId = :userId AND r.status IN :statuses")
    Optional<Long> findIdByVehicleIdAndUserIdAndStatusIn(Long vehicleId, Long userId,
                                                         List<ReservationStatus> statuses);

    @Query("SELECT r.id FROM Reservation r WHERE r.vehicleId = :vehicleId " +
            "AND r.userId = :userId AND r.status = :status " +
            "AND r.updatedAt BETWEEN :from AND :to")
    Optional<Long> findIdByVehicleIdAndUserIdAndStatusAndUpdatedAtBetween(Long vehicleId, Long userId,
                                                                          ReservationStatus returnStatus,
                                                                          LocalDateTime from, LocalDateTime now);

    @Query("""
            SELECT new com.pickcar.reservation.infrastructure.dto.ReservationDetailProjection(
                r.id,
                u.info.name,
                u.info.phoneNumber,
                v.info,
                r.dueDate,
                r.rentedAt
            )
            FROM Reservation r
            JOIN User u ON r.userId = u.id
            JOIN Vehicle v ON r.vehicleId = v.id
            WHERE r.id = :reservationId
            """)
    Optional<ReservationDetailProjection> findReservationDetailById(Long reservationId);

    @Query("""
            SELECT new com.pickcar.reservation.infrastructure.dto.AllocatedReservationInfoProjection(
            r.vehicleId,
            r.rentedAt,
            r.dueDate,
            r.status
            ) FROM Reservation r
            WHERE r.userId = :userId AND r.status IN :statuses
            """)
    AllocatedReservationInfoProjection findAllocatedReservationInfo(Long userId, List<ReservationStatus> statuses);

    Long countByStatusIn(List<ReservationStatus> reservationstatuses);

    Long countByStatus(ReservationStatus reservationStatus);

    Long countByStatusAndDueDateBetween(ReservationStatus reservationStatus, LocalDate from, LocalDate to);
}
