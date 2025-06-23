package com.pickcar.auth.presentation;

import com.pickcar.auth.application.LoginService;
import com.pickcar.auth.presentation.dto.request.LoginRequest;
import com.pickcar.auth.jwt.JwtProvider;
import com.pickcar.presentation.dto.response.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginService loginService;

    private final JwtProvider jwtProvider;

    @PostMapping("/login_after")
    @ResponseBody
    public ResponseEntity<SuccessResponse> login_after(@RequestBody LoginRequest request) {
        String token = loginService.login_after(request.email(), request.password());
        return ResponseEntity.ok(new SuccessResponse(200, token));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginRequest request , HttpSession session) {
        String result = loginService.login(request.email(), request.password(), session);
        return ResponseEntity.ok(new SuccessResponse(200,result));
    }
}