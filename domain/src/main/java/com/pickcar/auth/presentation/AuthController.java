package com.pickcar.auth.presentation;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.presentation.dto.request.AuthRequest;
import com.pickcar.auth.presentation.dto.response.AccessTokenResponse;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.security.jwt.JwtConstants;
import jakarta.servlet.http.Cookie;
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
        AuthResponse authResponse = authService.login(request.email(), request.password());
        addRefreshTokenToCookie(response, authResponse.refreshToken());
        return new AccessTokenResponse(authResponse.accessToken());
    } //TODO: 로그인 실패 예외처리 추가

    private void addRefreshTokenToCookie(HttpServletResponse response,String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (JwtConstants.REFRESH_TOKEN_VALIDITY / 1000));
//        cookie.setSecure(true); // HTTPS 환경에서만 쿠키가 전송되도록
        response.addCookie(cookie);
    } //TODO: 쿠키 설정 실패 예외처리 추가
}