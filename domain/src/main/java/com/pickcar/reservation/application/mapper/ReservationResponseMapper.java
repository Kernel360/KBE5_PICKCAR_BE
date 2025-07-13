package com.pickcar.reservation.application.mapper;

import com.pickcar.reservation.infrastructure.dto.EmployeeReservationProjection;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReservationResponseMapper {
    public ReservationPreInfoResponse toPreInfoResponse(List<EmployeeReservationProjection> projections) {
        return new ReservationPreInfoResponse(projections);
    }
}
