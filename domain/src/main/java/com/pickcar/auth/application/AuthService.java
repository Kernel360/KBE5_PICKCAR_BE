package com.pickcar.auth.application;

import com.pickcar.auth.domain.*;
import com.pickcar.auth.exception.AuthErrorCode;
import com.pickcar.auth.exception.AuthException;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.auth.presentation.dto.response.EmployeeListResponse;
import com.pickcar.auth.presentation.dto.response.UnAllocatedEmployeeResponse;
import com.pickcar.auth.validator.AuthValidator;
import com.pickcar.security.jwt.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    //TODO: 에러처리 하기
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] User Not Found By Id " + id));
    }

    //TODO: 에러처리 하기
    public List<User> getAllByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Transactional
    public void create(UserInfoRequest request) {
        authValidator.userRegisterRequest(request);

        User user = User.builder()
                .info(toUserInfoWithEncodedPassword(request))
                .role(request.isAdmin()? UserRole.SUPER_ADMIN : UserRole.EMPLOYEE)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
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

    @Transactional
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByInfoEmail(email)
                .orElseThrow(() ->  new AuthException(AuthErrorCode.INVALID_LOGIN_INFO));

        if (!passwordEncoder.matches(password, user.getInfo().getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_LOGIN_INFO);
        }

        String accessToken = jwtProvider.createAccessToken(
                user.getId(),
                user.getInfo().getName(),
                user.getRole().name()
        );

        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        tokenService.saveOrUpdateRefreshToken(user.getId(),refreshToken);
        return new AuthResponse(accessToken,refreshToken);
    }

    private UserInfo toUserInfoWithEncodedPassword(UserInfoRequest request) {
        return new UserInfo(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.phoneNumber()
        );
    }

    public List<UnAllocatedEmployeeResponse> getUnAllocatedEmployeeInfos(List<Long> allocatedUserIds) {
        List<User> unAllocatedUsers = userRepository.findAllByIdNotIn(allocatedUserIds);
        List<UnAllocatedEmployeeResponse> responses = new ArrayList<>();

        unAllocatedUsers.forEach(user -> {
            responses.add(UnAllocatedEmployeeResponse.from(user));
        });

        return responses;
    }

}

