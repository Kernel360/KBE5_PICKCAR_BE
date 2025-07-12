package com.pickcar.auth.application;

import com.pickcar.auth.domain.RefreshToken;
import com.pickcar.auth.domain.User;
import com.pickcar.auth.exception.AuthErrorCode;
import com.pickcar.auth.exception.AuthException;
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
    public AuthResponse reissueTokens(String refreshToken, boolean isExpired){
        //TODO : 변수명, 예외처리 수정하기
        //TODO: 일치하는 토큰 없으면 재로그인 요청(프론트)
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    //TODO: 쿠키에서 rt 삭제 필요
                    throw new TokenException(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND);
                });

        //TODO: 유저가 존재하지 않으면 RT 삭제 / 에러 메세지 출력(프론트)
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> {
                    deleteByToken(refreshToken);
                    return new AuthException(AuthErrorCode.USER_NOT_FOUND);
                });

        String newAccessToken = "";
        if(isExpired){
            log.info("만료된 토큰 발급");
            newAccessToken = jwtProvider.createExpiredAccessToken(
                    user.getId(),
                    user.getInfo().getName(),
                    user.getRole().name()
            );
        }else {
            log.info("정상 토큰 발급");
            newAccessToken = jwtProvider.createAccessToken(
                    user.getId(),
                    user.getInfo().getName(),
                    user.getRole().name()
            );
        }

        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());
        saveOrUpdateRefreshToken(user.getId(), newRefreshToken);
        log.info("newAccessToken: {}", newAccessToken);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void deleteByToken(String token){
        try {
            refreshTokenRepository.deleteByToken(token);
        } catch (Exception e) {
            log.warn("refreshToken 삭제 실패: {}", e.getMessage());
        }
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
