//package com.pickcar.reservation.application;
//
//import com.pickcar.DomainApplication;
//import com.pickcar.reservation.domain.Reservation;
//import com.pickcar.reservation.infrastructure.ReservationRepository;
//import com.pickcar.reservation.presentation.dto.request.ReservationCreateRequest;
//import java.time.LocalDateTime;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//@Transactional
//@ActiveProfiles(profiles = "test")
//@SpringBootTest(classes = DomainApplication.class)
//class ReservationServiceTest {
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Test
//    @DisplayName("예약의 생성과 ID 기반 조회가 가능하다")
//    void t001() {
//        ReservationCreateRequest testRequest = new ReservationCreateRequest(1L, 1L, LocalDateTime.now());
//        reservationService.create(testRequest);
//
//        Reservation reservation = reservationService.getById(1L);
//
//        Assertions.assertThat(reservation).isNotNull();
//        Assertions.assertThat(reservation.getCreatedAt())
//                .isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("잘못된 ID로 예약 조회를 시도하면 특정 예외가 발생한다")
//    void t002() {
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            reservationService.getById(0L);
//        }).withMessageContaining("Reservation Not Found By Id");
//    }
//}