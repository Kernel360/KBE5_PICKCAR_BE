package com.pickcar.auth.presentation;

import com.pickcar.auth.application.LoginService;
import com.pickcar.auth.presentation.dto.request.LoginRequest;
import com.pickcar.presentation.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginRequest request) {
        String token = loginService.login(request.email(), request.password());
        return ResponseEntity.ok(new SuccessResponse(200, token));
    }
}