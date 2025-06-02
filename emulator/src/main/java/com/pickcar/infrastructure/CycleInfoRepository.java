package com.pickcar.infrastructure;

import com.pickcar.domain.CycleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleInfoRepository extends JpaRepository<CycleInfo, Long> {
}
