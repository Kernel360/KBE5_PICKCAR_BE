package com.pickcar.reservation.presentation;

import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import com.pickcar.reservation.presentation.dto.response.SearchAbleVehiclesResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;

    //FIXME: 이것보단 ON되어 있는 차량들만 가져오는 걸로 바꾸는 게 좋을 듯(임시)
    @GetMapping("/vehicles/assignment-completed")
    @ResponseStatus(HttpStatus.OK)
    public List<SearchAbleVehiclesResponse> searchAssignedVehicles() {
        return reservationService.getAssignedVehicles();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void reservation(@RequestBody ReservationRequest request) {
        reservationService.reserving(request);
    }

    @PatchMapping("/return/{vehicleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitReturn(HttpServletRequest servletRequest, @PathVariable Long vehicleId) {
        reservationService.submitReturn(servletRequest, vehicleId);
    }

    @GetMapping("/pre-info")
    @ResponseStatus(HttpStatus.OK)
    public ReservationPreInfoResponse preInfo() {
        return reservationService.getReservationPreInfos();
    }
}
