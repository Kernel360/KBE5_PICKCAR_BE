package com.pickcar.dailyreport.infrastructure;

import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.infrastructure.dto.DestinationStatProjection;
import com.pickcar.dailyreport.infrastructure.dto.PastVehicleReservationStatProjection;
import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    @Query("""
            SELECT new com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection(
                (SELECT COUNT(v) FROM Vehicle v),
                (SELECT COUNT(r) FROM Reservation r WHERE r.status IN ('RESERVED', 'DELAYED')),
                (SELECT COUNT(r) FROM Reservation r WHERE r.status = 'DELAYED'),
                (SELECT COUNT(r) FROM Reservation r
                 WHERE r.status = 'RESERVED'
                 AND r.dueDate BETWEEN :today AND :after3Days)
            )
            """)
    VehicleReservationStatProjection findStaticInfo(LocalDate today, LocalDate after3Days);

    @Query("""
            SELECT new com.pickcar.dailyreport.infrastructure.dto.PastVehicleReservationStatProjection(
                dr.vehicleReservationStat
            )
            FROM DailyReport dr
            WHERE dr.reportDate = :date
            """)
    PastVehicleReservationStatProjection findStatByDate(LocalDate date);

    @Query("""
    SELECT new com.pickcar.dailyreport.infrastructure.dto.DestinationStatProjection(
        dh.destination,
        COUNT(dh.destination)
    )
    FROM DriveHistory dh
    WHERE DATE(dh.drivingEndedAt) = :yesterday
    AND dh.destination IS NOT NULL
    AND dh.destination != 'UNKNOWN'
    GROUP BY dh.destination
    ORDER BY COUNT(dh.destination) DESC
    """)
    List<DestinationStatProjection> findYesterDayDestinationStats(LocalDate yesterday);

    Optional<DailyReport> findByReportDate(LocalDate date);
}
