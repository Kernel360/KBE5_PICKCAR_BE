package com.pickcar.company.presentation;

import com.pickcar.company.application.CompanyService;
import com.pickcar.company.presentation.dto.response.CompanyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}