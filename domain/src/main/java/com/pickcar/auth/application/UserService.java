package com.pickcar.auth.application;

import com.pickcar.auth.domain.User;
import com.pickcar.auth.domain.UserInfo;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.domain.UserStatus;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.jwt.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] User Not Found By Id " + id));
    }

    @Transactional
    public void createEmployee(@AuthenticationPrincipal UserPrincipal principal,
                               UserInfoRequest request) {
        //토큰에 있는 회사ID 사용
        User employee = User.builder()
                .companyId(principal.getCompanyId())
                .info(toEncodedUserInfo(request))
                .role(UserRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(employee);
    }

    @Transactional
    public void createAdmin(UserInfoRequest request) {
        //요청에 있는 회사ID 사용
        User admin = User.builder()
                .companyId(request.companyId())
                .info(toEncodedUserInfo(request))
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(admin);
    }

    private UserInfo toEncodedUserInfo(UserInfoRequest request) {
        return new UserInfo(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.phoneNumber()
        );
    }

    public List<User> getAllByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }
}
