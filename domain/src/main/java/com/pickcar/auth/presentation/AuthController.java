package com.pickcar.auth.presentation;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.presentation.dto.request.AuthRequest;
import com.pickcar.auth.presentation.dto.response.AccessTokenResponse;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.security.jwt.JwtConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse login(@RequestBody AuthRequest request, HttpServletResponse response) {
        log.info("User login request received");
        AuthResponse authResponse = authService.login(request.email(), request.password());
        setRefreshTokenCookie(response, authResponse.refreshToken());
        setAccessTokenCookie(response, authResponse.accessToken());
        return new AccessTokenResponse(authResponse.accessToken());
    } //TODO: 로그인 실패 예외처리 추가



    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("User logout request received");
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken != null && !refreshToken.isBlank()) {
            try {
                authService.deleteByToken(refreshToken);
            } catch (Exception e) {
                log.warn("로그아웃 중 RT 삭제 실패: {}", e.getMessage());
            }
        }

        setRefreshTokenCookie(response,null);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) { //TODO: 자주 사용되서 Util로 빼기
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        if (refreshToken == null) {
            cookie.setMaxAge(0); // 삭제
        } else {
            cookie.setMaxAge((int) (JwtConstants.REFRESH_TOKEN_VALIDITY / 1000));
//        cookie.setSecure(true); // HTTPS 환경에서만 쿠키가 전송되도록
        }
        response.addCookie(cookie);
    }

    private void setAccessTokenCookie(HttpServletResponse response, String accessToken) { //TODO: 자주 사용되서 Util로 빼기
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        if (accessToken == null) {
            cookie.setMaxAge(0); // 삭제
        } else {
            cookie.setMaxAge((int) (JwtConstants.REFRESH_TOKEN_VALIDITY / 1000));
//        cookie.setSecure(true); // HTTPS 환경에서만 쿠키가 전송되도록
        }
        response.addCookie(cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) { //TODO: 자주 사용되서 Util로 빼기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null; // 쿠키에 refreshToken이 없으면 null 반환
    }
}