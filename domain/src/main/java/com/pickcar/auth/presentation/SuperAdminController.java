package com.pickcar.auth.presentation;

import com.pickcar.auth.application.SuperAdminService;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SuperAdminController {
    private final SuperAdminService superAdminService;

    @PostMapping("/super-admins")
    public void createSuperAdmin(@RequestBody UserInfoRequest request){
        superAdminService.create(request);
    }
}
