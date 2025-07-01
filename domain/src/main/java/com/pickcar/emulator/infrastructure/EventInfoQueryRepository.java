package com.pickcar.emulator.infrastructure;

import com.pickcar.emulator.domain.EventInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoQueryRepository extends JpaRepository<EventInfo, Long> {
}
