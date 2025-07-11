package com.pickcar.auth.application;

import com.pickcar.auth.domain.User;
import com.pickcar.auth.domain.UserInfo;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.domain.UserStatus;
import com.pickcar.auth.exception.UserErrorCode;
import com.pickcar.auth.exception.UserException;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.auth.presentation.dto.response.EmployeeListResponse;
import com.pickcar.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] User Not Found By Id " + id));
    }

    @Transactional
    public void create(UserInfoRequest request) {

        if (userRepository.existsByInfoEmail(request.email())) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }

        User user = User.builder()
                .info(toUserInfoWithEncodedPassword(request))
                .role(request.isAdmin()? UserRole.SUPER_ADMIN : UserRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
    }

    private UserInfo toUserInfoWithEncodedPassword(UserInfoRequest request) {
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

    public List<EmployeeListResponse> getAllEmployees() {
        List<User> users = userRepository.findAllByRole(UserRole.EMPLOYEE);
        List<EmployeeListResponse> responses = new ArrayList<>();

        users.forEach(user -> {
            EmployeeListResponse response = EmployeeListResponse.from(user);
            responses.add(response);
        });

        return responses;
    }

    // TODO 보안처리 고려
    public UserRole findUserRole(HttpServletRequest request) {
        Long userId = jwtProvider.extractUserId(request);
        return userRepository.findById(userId)
                .map(User::getRole)
                .orElseThrow(NoSuchElementException::new);
    }
}
