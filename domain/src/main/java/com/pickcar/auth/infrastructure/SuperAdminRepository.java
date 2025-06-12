package com.pickcar.auth.infrastructure;

import com.pickcar.auth.domain.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {
    Optional<SuperAdmin> findByInfoEmail(String email);
}
