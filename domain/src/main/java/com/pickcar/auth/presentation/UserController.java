package com.pickcar.auth.presentation;

import com.pickcar.auth.application.UserService;
import com.pickcar.auth.presentation.dto.request.TestRequest;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public void registerEmployee(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestBody UserInfoRequest request) {
        userService.createEmployee(principal,request);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/admins")
    public void registerAdmin(@RequestBody UserInfoRequest request) {
        userService.createAdmin(request);
    }

    @PostMapping("/test")
    public void test(@RequestBody UserInfoRequest testRequest) {
        return;
    }
}
