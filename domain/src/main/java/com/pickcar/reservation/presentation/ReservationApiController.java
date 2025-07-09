package com.pickcar.reservation.presentation;

import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;
    private final JwtProvider jwtProvider;

    //FIXME: 로직도 그렇고 사실 VEHICLE이 맞는듯 근데 일단 진행
    @GetMapping("/vehicles")
    @ResponseStatus(HttpStatus.OK)
    public List<SearchAbleVehiclesResponse> searchAbleVehicles() {
        return reservationService.getAbleVehicles();
    }

    //FIXME: 이것보단 ON되어 있는 차량들만 가져오는 걸로 바꾸는 게 좋을 듯(임시)
    @GetMapping("/vehicles/done")
    @ResponseStatus(HttpStatus.OK)
    public List<SearchAbleVehiclesResponse> getDisabledVehicles() {
        return reservationService.getDisabledVehicles();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void reservation(@RequestBody ReservationRequest request) {
        reservationService.reservation(request);
    }
}
