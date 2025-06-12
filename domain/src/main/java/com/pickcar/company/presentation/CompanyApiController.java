package com.pickcar.company.presentation;

import com.pickcar.company.application.CompanyService;
import com.pickcar.company.presentation.dto.request.CompanyJoinRequest;
import com.pickcar.company.presentation.dto.response.CompanyListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyApiController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyListResponse>> getAllCompanies() {
        List<CompanyListResponse> responses = companyService.findAll(); // 서비스에 맞는 메소드 호출
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public void createCompany(@RequestBody CompanyJoinRequest request) {
        log.info("POST CompanyJoinRequest : {} ", request);
        companyService.create(request);
    }
}