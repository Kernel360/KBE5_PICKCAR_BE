package com.pickcar.auth.infrastructure;

import com.pickcar.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByInfoEmail(String email);
}
