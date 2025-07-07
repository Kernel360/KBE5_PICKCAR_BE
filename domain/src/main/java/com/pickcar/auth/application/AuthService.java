package com.pickcar.auth.application;

import com.pickcar.auth.domain.*;
import com.pickcar.auth.exception.AuthErrorCode;
import com.pickcar.auth.exception.AuthException;
import com.pickcar.auth.infrastructure.RefreshTokenRepository;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.auth.presentation.dto.response.EmployeeListResponse;
import com.pickcar.security.jwt.JwtConstants;
import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService { 

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

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

        if (userRepository.existsByInfoEmail(request.email())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }

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
        //TODO: 예외처리 하기
        User user = userRepository.findByInfoEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("입력한 이메일이 유효하지 않거나 존재하지 않는 사용자입니다."));

        //TODO: 예외처리 하기
        if (!passwordEncoder.matches(password, user.getInfo().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(
                user.getId(),
                user.getInfo().getName(),
                user.getRole().name()
        );

        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        saveOrUpdateRefreshToken(user.getId(),refreshToken);
        return new AuthResponse(accessToken,refreshToken);
    }

    @Transactional
    public AuthResponse reissueTokens(String refreshToken){
        //TODO : 변수명, 예외처리 수정하기
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("token이 없음"));

        if(token.isExpired()){ //TODO: 없는 경우 예외처리 수정하기
            throw new IllegalStateException("Refresh token has expired.");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String newAccessToken = jwtProvider.createAccessToken(
                user.getId(),
                user.getInfo().getName(),
                user.getRole().name()
        );

        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());
        saveOrUpdateRefreshToken(user.getId(), newRefreshToken);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

    private void saveOrUpdateRefreshToken(Long userId,String refreshToken){
        //TODO: 해시화 해서 저장하기
        LocalDateTime expiryDate = JwtUtils.calculateExpiryDate(JwtConstants.REFRESH_TOKEN_VALIDITY);
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        token -> token.update(refreshToken,expiryDate),
                        () -> refreshTokenRepository.save(RefreshToken.create(
                                userId,
                                refreshToken,
                                JwtUtils.calculateExpiryDate(JwtConstants.REFRESH_TOKEN_VALIDITY)
                        ))//TODO: 토큰 저장 실패 예외처리 하기
                );
    }

    private UserInfo toUserInfoWithEncodedPassword(UserInfoRequest request) {
        return new UserInfo(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.phoneNumber()
        );
    }
}

