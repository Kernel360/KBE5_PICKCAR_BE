package com.pickcar.reservation.application;

import com.pickcar.auth.application.UserService;
import com.pickcar.auth.domain.User;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.exception.ReservationErrorCode;
import com.pickcar.reservation.exception.ReservationException;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import com.pickcar.reservation.presentation.dto.request.ReservationCreateRequest;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    @Value(value = "${custom.reservation.coolDownMinutes}")
    private Long coolDownMinutes;

    private final UserService userService;
    private final VehicleService vehicleService;
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

    public Reservation getActiveReservationForDriveHistory(Long vehicleId) {
        try {
            return getActiveReservation(vehicleId);
        } catch (ReservationException e) {
            return getLatestValidReservation(vehicleId);
            //TODO: 여기서도 찾지 못했다면 어떻게 대처할 것인지
        }
    }

    private Reservation getActiveReservation(Long vehicleId) {
        Optional<Reservation> maybeReservation = reservationRepository.findByVehicleIdAndStatus(vehicleId,
                ReservationStatus.RESERVED);

        if (maybeReservation.isPresent()) {
            //FIXME: Optional.get()을 두 번 사용중
            return maybeReservation.get();
        }

        throw new ReservationException(ReservationErrorCode.NOT_FOUND_ACTIVE_RESERVATION_BY_VEHICLE_ID);
    }

    private Reservation getLatestValidReservation(Long vehicleId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime coolDownMinutesAgo = now.minusMinutes(coolDownMinutes);

        Optional<Reservation> maybeReservation = reservationRepository.findByVehicleIdAndUpdatedAtBetween(
                vehicleId, coolDownMinutesAgo, now);

        if (maybeReservation.isPresent()) {
            //FIXME: Optional.get()을 두 번 사용중
            log.info("최근 기반으로 탐색된 예약 ID : {}", maybeReservation.get().getId());
            return maybeReservation.get();
        }

        throw new ReservationException(ReservationErrorCode.NOT_FOUND_LATEST_UPDATED_RESERVATION);
    }

    public ReservationContext getReservationContextById(Long reservationId) {
        Reservation reservation = getById(reservationId);
        User user = userService.getById(reservation.getUserId());
        Vehicle vehicle = vehicleService.getById(reservation.getVehicleId());

        return new ReservationContext(reservation, user.getInfo(), vehicle.getInfo());
    }
}
