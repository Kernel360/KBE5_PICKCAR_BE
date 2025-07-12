package com.pickcar.emulator.infrastructure;

import com.pickcar.emulator.domain.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleInfoRepository extends JpaRepository<Cycle, Long> {
}
