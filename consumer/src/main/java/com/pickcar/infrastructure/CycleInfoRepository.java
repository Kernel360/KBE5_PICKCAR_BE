package com.pickcar.infrastructure;

import com.pickcar.domain.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CycleInfoRepository extends JpaRepository<Cycle, Long> {
}
