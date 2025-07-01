//package com.pickcar.vehicle.application;
//
//import com.pickcar.DomainApplication;
//import com.pickcar.vehicle.domain.FuelType;
//import com.pickcar.vehicle.domain.Vehicle;
//import com.pickcar.vehicle.domain.VehicleInfo;
//import com.pickcar.vehicle.infrastructure.VehicleRepository;
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
//class VehicleServiceTest {
//
//    @Autowired
//    private VehicleService vehicleService;
//
//    @Autowired
//    private VehicleRepository vehicleRepository;
//
//    @Test
//    @DisplayName("자동차가 생성되고, Id 기반 조회가 가능")
//    void t001() {
//        //FIXME: 제거 필요
//        VehicleInfo testInfo = new VehicleInfo("model", "color", "licensePlate",
//                "carAge", "brandName", FuelType.DIESEL);
//
//        vehicleService.create(testInfo);
//
//        Vehicle vehicle = vehicleService.getById(1L);
//
//        Assertions.assertThat(vehicle).isNotNull();
//        Assertions.assertThat(vehicle.getCreatedAt())
//                .isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("자동차 번호판이 동일하게 생성되려 한다면, 특정 예외를 반환")
//    void t002() {
//        //FIXME: 제거 필요
//        VehicleInfo testInfo = new VehicleInfo("model", "color", "licensePlate",
//                "carAge", "brandName", FuelType.DIESEL);
//
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            vehicleService.create(testInfo);
//            vehicleService.create(testInfo);
//        });
//    }
//
//    @Test
//    @DisplayName("해당 ID를 가진 자동차를 찾지 못한다면 특정 예외를 반환")
//    void t003() {
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            vehicleService.getById(0L);
//        });
//    }
//}