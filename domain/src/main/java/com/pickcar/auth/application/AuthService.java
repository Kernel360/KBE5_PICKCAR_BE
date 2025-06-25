package com.pickcar.auth.application;

import com.pickcar.auth.domain.RefreshToken;
import com.pickcar.auth.domain.User;
import com.pickcar.auth.infrastructure.RefreshTokenRepository;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.security.jwt.JwtConstants;
import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService { 

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse login(String email, String password) { //TODO: 메소드명 변경하기
        User user = findUserByEmail(email);
        validatePassword(password, user.getInfo().getPassword());
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user.getId());
        saveOrUpdateRefreshToken(user.getId(),refreshToken);
        return new AuthResponse(accessToken,refreshToken);
    }

    @Transactional
    public AuthResponse newRefreshToken(String refreshToken){
        //TODO : 변수명, 예외처리 수정하기
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("token이 없음"));

        if(token.isExpired()){ //TODO: 없는 경우 예외처리 수정하기
            throw new IllegalStateException("Refresh token has expired.");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user.getId());
        saveOrUpdateRefreshToken(user.getId(), newRefreshToken);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

    private User findUserByEmail(String email){ //TODO: 예외처리 하기
        return userRepository.findByInfoEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("입력한 이메일이 유효하지 않거나 존재하지 않는 사용자입니다."));
    }//BadCredentialsException 

    private void validatePassword(String rawPassword, String encodedPassword){
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");//TODO: 예외처리 하기
        }
    }

    private String generateAccessToken(User user) {
        return jwtProvider.createAccessToken(
                user.getId(),
                user.getInfo().getName(),
                user.getRole().name()
        );
    }

    private String generateRefreshToken(Long userId) {
        return jwtProvider.createRefreshToken(userId);
    }

    private void createRefreshToken(Long userId,String refreshToken){
        refreshTokenRepository.save(RefreshToken.create(
                userId,
                refreshToken,
                JwtUtils.calculateExpiryDate(JwtConstants.REFRESH_TOKEN_VALIDITY)
        )); //TODO: 토큰 저장 실패 예외처리 하기
    }

    private void saveOrUpdateRefreshToken(Long userId,String refreshToken){
        //TODO: 해시화 해서 저장하기
        LocalDateTime expiryDate = JwtUtils.calculateExpiryDate(JwtConstants.REFRESH_TOKEN_VALIDITY);
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        token -> token.update(refreshToken,expiryDate),
                        () -> refreshTokenRepository.save(
                                RefreshToken.create(userId,refreshToken,expiryDate)
                        )
                );
    }
}

