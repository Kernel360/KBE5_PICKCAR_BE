//package com.pickcar.drivehistory.application;
//
//import com.pickcar.DomainApplication;
//import com.pickcar.drivehistory.domain.DriveHistory;
//import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
//import com.pickcar.drivehistory.exception.DriveHistoryException;
//import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
//import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryCreateRequest;
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
//class DriveHistoryServiceTest {
//
//    @Autowired
//    private DriveHistoryService driveHistoryService;
//
//    @Autowired
//    private DriveHistoryRepository driveHistoryRepository;
//
//    @Test
//    @DisplayName("운행 일지가 생성되고, ID 기반 조회가 가능함")
//    void t001() {
//        LocalDateTime now = LocalDateTime.now();
//        DriveHistoryCreateRequest testRequest = new DriveHistoryCreateRequest(1L, now, now, now, 1.23D);
//        driveHistoryService.create(testRequest);
//
//        DriveHistory history = driveHistoryService.getById(1L);
//
//        Assertions.assertThat(history).isNotNull();
//        Assertions.assertThat(history.getCreatedAt())
//                .isBetween(java.time.LocalDateTime.now().minusMinutes(1), java.time.LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("ID 기반 조회시, 잘못된 ID 요청인 경우 특정 예외가 발생함")
//    void t002() {
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            driveHistoryService.getById(0L);
//        }).withMessageContaining("DriveHistory Not Found By Id");
//    }
//
//    @Test
//    @DisplayName("운행 시작 일시는 현재 시각보다 빠를 수 없음")
//    void t003() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime future = now.plusMinutes(1);
//        DriveHistoryCreateRequest testRequest1 = new DriveHistoryCreateRequest(1L, future, now, now, 1.23D);
//
//        Assertions.assertThatExceptionOfType(DriveHistoryException.class).isThrownBy(() -> {
//            driveHistoryService.create(testRequest1);
//        }).withMessageContaining(DriveHistoryErrorCode.START_TIME_BEFORE_NOW.getReason());
//    }
//
//    @Test
//    @DisplayName("운행 종료 일시는 현재 시각보다 빠를 수 없음")
//    void t004() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime future = now.plusMinutes(1);
//        DriveHistoryCreateRequest testRequest1 = new DriveHistoryCreateRequest(1L, now, future, now, 1.23D);
//
//        Assertions.assertThatExceptionOfType(DriveHistoryException.class).isThrownBy(() -> {
//            driveHistoryService.create(testRequest1);
//        }).withMessageContaining(DriveHistoryErrorCode.END_TIME_BEFORE_NOW.getReason());
//    }
//
//    @Test
//    @DisplayName("운행 종료 일시는 운행 시작 일시보다 빠를 수 없음")
//    void t005() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime past = now.minusMinutes(1);
//        DriveHistoryCreateRequest testRequest1 = new DriveHistoryCreateRequest(1L, now, past, now, 1.23D);
//
//        Assertions.assertThatExceptionOfType(DriveHistoryException.class).isThrownBy(() -> {
//            driveHistoryService.create(testRequest1);
//        }).withMessageContaining(DriveHistoryErrorCode.END_TIME_BEFORE_START_TIME.getReason());
//    }
//}