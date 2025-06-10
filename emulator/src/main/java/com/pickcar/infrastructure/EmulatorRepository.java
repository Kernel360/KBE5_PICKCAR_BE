package com.pickcar.infrastructure;

import com.pickcar.emulator.domain.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmulatorRepository extends JpaRepository<Terminal, Long> {

}
