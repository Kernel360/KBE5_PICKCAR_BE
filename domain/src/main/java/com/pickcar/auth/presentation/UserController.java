package com.pickcar.auth.presentation;

import com.pickcar.auth.application.UserService;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up/admins")
    public void registerAdmin(@RequestBody UserInfoRequest request) {
        log.info("CREATE ADMIN");
        userService.create(request, UserRole.SUPER_ADMIN);
    }


    @PostMapping("/sign-up")
    public void registerEmployee(@RequestBody UserInfoRequest request) {
        log.info("CREATE EMPLOYEE");
        userService.create(request, UserRole.EMPLOYEE);
    }
}
