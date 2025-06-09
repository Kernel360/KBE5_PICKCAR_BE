package com.pickcar.infrastructure;

import com.pickcar.emulator.domain.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
}
