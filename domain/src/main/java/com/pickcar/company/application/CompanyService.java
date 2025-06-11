package com.pickcar.company.application;

import com.pickcar.company.domain.Company;
import com.pickcar.company.infrastructure.CompanyRepository;
import com.pickcar.company.presentation.dto.request.CompanyJoinRequest;
import com.pickcar.company.presentation.dto.response.CompanyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Company create(CompanyJoinRequest request) {
        checkCondition(request);

        Company company = Company.builder()
                .name(request.name())
                .address(request.address())
                .businessPhoneNumber(request.businessPhoneNumber())
                .email(request.email())
                .description(request.description())
                .businessNumber(request.businessNumber())
                .contractStatus(request.contractStatus())
                .build();

        return companyRepository.save(company);     //FIXME: 반환 금지 
    }

    public Company getById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Company Not Found By Id " + id));
    }

    //FIXME: 메서드 이름 다시 짓기
    private void checkCondition(CompanyJoinRequest request) {
        checkNameCondition(request.name());
        checkBusinessNumberCondition(request.businessNumber());
    }

    //FIXME: 메서드 이름 다시 짓기
    private void checkBusinessNumberCondition(String businessNumber) {
        if (companyRepository.findByBusinessNumber(businessNumber).isPresent()) {
            throw new IllegalArgumentException("[ERROR] 중복된 사업자 번호는 사용할 수 없습니다. Business Number : "
                    + businessNumber);
        }

    }

    //FIXME: 메서드 이름 다시 짓기
    private void checkNameCondition(String name) {
        if (companyRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("[ERROR] 이미 사용중인 회사명 입니다. Company Name : " + name);
        }
    }

    public List<CompanyListResponse> findAll() {
        List<Company> companies = companyRepository.findAll();

        List<CompanyListResponse> responseList = new ArrayList<>();

        for (Company company : companies) {
            CompanyListResponse dto = CompanyListResponse.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .build();
            responseList.add(dto);
        }

        return responseList;
    }
}
