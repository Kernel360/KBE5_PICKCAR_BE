//package com.pickcar.rental.application;
//
//import com.pickcar.DomainApplication;
//import com.pickcar.rental.domain.Rental;
//import com.pickcar.rental.infrastructure.RentalRepository;
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
//class RentalServiceTest {
//
//    @Autowired
//    private RentalService rentalService;
//
//    @Autowired
//    private RentalRepository rentalRepository;
//
//    @Test
//    @DisplayName("렌탈 내역 생성 및 id 기반 조회")
//    void t001() {
//        rentalService.create();
//
//        Rental rental = rentalService.getById(1L);
//
//        Assertions.assertThat(rental).isNotNull();
//        Assertions.assertThat(rental.getCreatedAt())
//                .isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("잘못된 id로 렌탈 내역 조회를 시도하면 특정 예외가 발생함")
//    void t002() {
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            rentalService.getById(0L);
//        }).withMessageContaining("Rental Not Found By Id");
//    }
//
//}