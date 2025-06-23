package com.pickcar.auth.application;

import com.pickcar.auth.domain.SuperAdmin;
import com.pickcar.auth.domain.User;
import com.pickcar.auth.infrastructure.SuperAdminRepository;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.jwt.JwtProvider;
import jakarta.servlet.http.HttpSession;
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

    public String login_after(String email, String reqPassword) {
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

    public String login(String email, String password, HttpSession session) {
        return superAdminRepository.findByInfoEmail(email)
                .map(superAdmin -> {
                    String storedPassword = superAdmin.getInfo().getPassword();
                    if (!passwordEncoder.matches(password, storedPassword)) {
                        return "비밀번호가 일치하지 않습니다.";
                    }

                    session.setAttribute("superAdmin", superAdmin.getInfo());
                    return "success";
                })
                .orElseGet(() -> "존재하지 않는 사용자입니다.");
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

