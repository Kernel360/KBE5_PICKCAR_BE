package com.pickcar.reservation.presentation;

import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.AllocatedReservationInfo;
import com.pickcar.reservation.presentation.dto.response.ReservationDetailResponse;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import com.pickcar.security.principal.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void reservation(@RequestBody ReservationRequest request) {
        reservationService.reserving(request);
    }

    @GetMapping("/pre-info")
    @ResponseStatus(HttpStatus.OK)
    public ReservationPreInfoResponse preInfo() {
        return reservationService.getReservationPreInfos();
    }

    @GetMapping("/{reservationId}/detail")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDetailResponse detail(@PathVariable Long reservationId) {
        return reservationService.getDetailResponse(reservationId);
    }

    @GetMapping("/allocation")
    @ResponseStatus(HttpStatus.OK)
    public AllocatedReservationInfo findAllocation(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return reservationService.getIdByUserIdFromReservation(userDetails.getId());
    }
}
