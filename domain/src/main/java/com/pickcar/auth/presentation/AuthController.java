package com.pickcar.auth.presentation;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.application.TokenService;
import com.pickcar.auth.presentation.dto.request.AuthRequest;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.auth.presentation.dto.response.AccessTokenResponse;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.auth.presentation.dto.response.EmployeeListResponse;
import com.pickcar.auth.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserInfoRequest request) {
        authService.create(request);
    }

    @GetMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeListResponse> getEmployees() {
        List<EmployeeListResponse> responses = authService.getAllEmployees();
        return responses;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse login(@RequestBody AuthRequest request, HttpServletResponse response) {
        log.info("User login request received");
        AuthResponse authResponse = authService.login(request.email(), request.password());
        TokenUtils.setRefreshTokenCookie(response, authResponse.refreshToken());
        return new AccessTokenResponse(authResponse.accessToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("User logout request received");
        String refreshToken = TokenUtils.extractRefreshTokenFromCookie(request);

        if (refreshToken != null && !refreshToken.isBlank()) {
            tokenService.deleteByToken(refreshToken);
        }

        TokenUtils.setRefreshTokenCookie(response,null);
    }
}