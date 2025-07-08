package com.pickcar.auth.application;

import com.pickcar.auth.domain.RefreshToken;
import com.pickcar.auth.domain.User;
import com.pickcar.auth.exception.TokenErrorCode;
import com.pickcar.auth.exception.TokenException;
import com.pickcar.auth.infrastructure.RefreshTokenRepository;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.security.jwt.JwtConstants;
import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public void saveOrUpdateRefreshToken(Long userId,String refreshToken){
        LocalDateTime expiryDate = JwtUtils.calculateExpiryDate(JwtConstants.REFRESH_TOKEN_VALIDITY);
        try {
            refreshTokenRepository.findByUserId(userId)
                    .ifPresentOrElse(
                            token -> token.update(refreshToken,expiryDate),
                            () -> refreshTokenRepository.save(RefreshToken.create(
                                    userId,
                                    refreshToken,
                                    JwtUtils.calculateExpiryDate(JwtConstants.REFRESH_TOKEN_VALIDITY)
                            ))
                    );
        }catch (Exception e){
            log.error("RefreshToken DB 저장 실패");
            throw new TokenException(TokenErrorCode.REFRESH_TOKEN_SAVE_FAILED);
        }
    }
}
