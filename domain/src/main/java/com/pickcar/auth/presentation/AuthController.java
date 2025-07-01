package com.pickcar.auth.presentation;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.presentation.dto.request.AuthRequest;
import com.pickcar.auth.presentation.dto.response.AccessTokenResponse;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.security.jwt.JwtConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.pickcar.auth.application.LoginService;
import com.pickcar.auth.presentation.dto.request.AuthRequest;
import com.pickcar.jwt.JwtProvider;
import com.pickcar.presentation.dto.response.SuccessResponse;
import jakarta.servlet.http.HttpSession;
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
        return new AccessTokenResponse(authResponse.accessToken());
    } //TODO: 로그인 실패 예외처리 추가



    @PostMapping("/login_after")
    @ResponseBody
    public ResponseEntity<SuccessResponse> login_after(@RequestBody AuthRequest request) {
        String token = loginService.login_after(request.email(), request.password());
        return ResponseEntity.ok(new SuccessResponse(200, token));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody AuthRequest request , HttpSession session) {
        String result = loginService.login(request.email(), request.password(), session);
        return ResponseEntity.ok(new SuccessResponse(200,result));
    }
}