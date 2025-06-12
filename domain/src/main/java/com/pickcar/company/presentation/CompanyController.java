package com.pickcar.company.presentation;

import com.pickcar.company.application.CompanyService;
import com.pickcar.company.domain.Company;
import com.pickcar.company.presentation.dto.request.CompanyJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    public void createCompany(@RequestBody CompanyJoinRequest request) {
        log.info("POST CompanyJoinRequest : {} ", request);
        companyService.create(request);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompany(){
        List<Company> companies = companyService.getAll();
        return ResponseEntity.ok(companies);
    }
}

