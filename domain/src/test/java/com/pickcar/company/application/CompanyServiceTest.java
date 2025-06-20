//package com.pickcar.company.application;
//
//import com.pickcar.DomainApplication;
//import com.pickcar.company.domain.Company;
//import com.pickcar.company.domain.ContractStatus;
//import com.pickcar.company.infrastructure.CompanyRepository;
//import com.pickcar.company.presentation.dto.request.CompanyJoinRequest;
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
//class CompanyServiceTest {
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Test
//    @DisplayName("회사가 생성되고, id 기반 조회가 가능함")
//    void t001() {
//        CompanyJoinRequest testRequest = new CompanyJoinRequest("테스트 회사명", "테스트 회사 주소", "테스트 회사 번호",
//                "테스트 회사 이메일", "테스트 설명", "테스트 사업자 번호", ContractStatus.ACTIVE);
//        companyService.create(testRequest);
//
//        Company company = companyService.getById(1L);
//
//        Assertions.assertThat(company).isNotNull();
//        Assertions.assertThat(company.getCreatedAt())
//                .isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("회사의 이름은 같을 수 없음")
//    void t002() {
//        CompanyJoinRequest testRequest1 = new CompanyJoinRequest("중복 회사명", "", "",
//                "", "", "", ContractStatus.ACTIVE);
//        CompanyJoinRequest testRequest2 = new CompanyJoinRequest("중복 회사명", "", "",
//                "", "", "", ContractStatus.ACTIVE);
//
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            companyService.create(testRequest1);
//            companyService.create(testRequest2);
//        }).withMessageContaining("회사명");
//    }
//
//    @Test
//    @DisplayName("회사의 사업자 등록 번호는 같을 수 없음")
//    void t003() {
//        CompanyJoinRequest testRequest1 = new CompanyJoinRequest("123", "", "",
//                "", "", "중복 사업자 번호", ContractStatus.ACTIVE);
//        CompanyJoinRequest testRequest2 = new CompanyJoinRequest("234", "", "",
//                "", "", "중복 사업자 번호", ContractStatus.ACTIVE);
//
//        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
//            companyService.create(testRequest1);
//            companyService.create(testRequest2);
//        }).withMessageContaining("사업자 번호");
//    }
//}