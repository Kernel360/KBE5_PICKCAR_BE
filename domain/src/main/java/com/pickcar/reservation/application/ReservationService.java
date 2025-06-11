package com.pickcar.reservation.application;

import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.presentation.dto.request.ReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public void create(ReservationCreateRequest request) {
        //TODO: 유효성 검사 필요
        Reservation reservation = Reservation.builder()
                .userId(request.userId())
                .vehicleId(request.vehicleId())
                .rentedAt(request.rentedAt())
                .returnedAt(null)                        //FIXME: 반납 시기를 정하기 VS 반납 했을때를 기록하기
                .status(ReservationStatus.RESERVED)     //FIXME: Default로 "예약" 상태로 생성?
                .build();

        reservationRepository.save(reservation);
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Reservation Not Found By Id " + id));
    }

    //status가 할당중인것 중 자동차가 일치하는 것 =>status 변경이 누락된 경우에 대한 예외처리 필요
    //NOTE: 만약 즉시 예약상태가 종료되었다면?
    public Reservation getActiveReservationByVehicleId(Long vehicleId) {
        return reservationRepository.findByVehicleIdAndStatus(vehicleId, ReservationStatus.RESERVED).orElseThrow(
                () -> new IllegalArgumentException("예약중인 차량 중 해당 차량을 찾을 수 없습니다."));
    }
}
