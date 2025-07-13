package com.pickcar.reservation.application.mapper;

import com.pickcar.reservation.infrastructure.dto.AllocatedReservationInfoProjection;
import com.pickcar.reservation.infrastructure.dto.EmployeeReservationProjection;
import com.pickcar.reservation.infrastructure.dto.ReservationDetailProjection;
import com.pickcar.reservation.presentation.dto.response.AllocatedReservationInfo;
import com.pickcar.reservation.presentation.dto.response.ReservationDetailResponse;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReservationResponseMapper {
    // NOTE: 현재는 projection이 한개라 과한 구조이지만 추가 조회가 들어가면 유용

    public ReservationPreInfoResponse toPreInfoResponse(List<EmployeeReservationProjection> projections) {
        return new ReservationPreInfoResponse(projections);
    }

    public ReservationDetailResponse toDetailResponse(ReservationDetailProjection projection) {
        return new ReservationDetailResponse(
                projection.reservationId(),
                projection.employeeName(),
                projection.employeePhoneNumber(),
                projection.vehicleInfo(),
                projection.dueDate(),
                projection.rentedAt()
        );
    }

    public AllocatedReservationInfo toAllocatedReservationInfo(AllocatedReservationInfoProjection projection) {
        return new AllocatedReservationInfo(
                projection.vehicleId(),
                projection.rentedAt(),
                projection.dueDate(),
                projection.status()
        );
    }
}
