package com.pickcar.auth.application;

import com.pickcar.auth.domain.*;
import com.pickcar.auth.infrastructure.SuperAdminRepository;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperAdminService { //TODO: 제거하기

    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(UserInfoRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        SuperAdmin superAdmin = SuperAdmin.builder()
                .info(new UserInfo(
                        request.email(),
                        encodedPassword,
                        request.name(),
                        request.phoneNumber()
                ))
                .status(UserStatus.ACTIVE)
                .build();

        superAdminRepository.save(superAdmin);
    }

    public SuperAdmin getById(Long id) {
        return superAdminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Super Admin Not Found By Id :" + id));
    }
}
