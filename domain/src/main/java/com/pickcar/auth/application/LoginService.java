package com.pickcar.auth.application;

import com.pickcar.auth.domain.SuperAdmin;
import com.pickcar.auth.domain.User;
import com.pickcar.auth.infrastructure.SuperAdminRepository;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final SuperAdminRepository superAdminRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder; // BCrypt 추천

    public String login(String email, String reqPassword) {
        // 1. SuperAdmin
        Optional<SuperAdmin> superAdminOpt = superAdminRepository.findByInfoEmail(email);
        if (superAdminOpt.isPresent()) {
            SuperAdmin superAdmin = superAdminOpt.get();
            return authenticateAndGenerateToken(
                    reqPassword,
                    superAdmin.getInfo().getPassword(),
                    superAdmin.getId(),
                    0L,
                    superAdmin.getRole().name()
            );
        }

        // 2. User
        Optional<User> userOpt = userRepository.findByInfoEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return authenticateAndGenerateToken(
                    reqPassword,
                    user.getInfo().getPassword(),
                    user.getId(),
                    user.getCompanyId(),
                    user.getRole().name()
            );
        }

        throw new IllegalArgumentException("사용자 없음");
    }

    // 인증 및 JWT 생성 공통 메서드
    private String authenticateAndGenerateToken(String rawPassword,
                                                String encodedPassword,
                                                Long userId,
                                                Long companyId,
                                                String role) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }
        return jwtProvider.generateToken(userId, companyId, role);
    }
}

