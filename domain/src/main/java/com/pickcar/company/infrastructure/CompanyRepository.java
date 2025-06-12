package com.pickcar.company.infrastructure;

import com.pickcar.company.domain.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Optional<Company> findByBusinessNumber(String businessNumber);

    Optional<Company> findById(Long companyId);
}
