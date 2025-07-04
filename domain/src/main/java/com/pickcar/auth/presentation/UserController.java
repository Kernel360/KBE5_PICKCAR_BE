package com.pickcar.auth.presentation;

import com.pickcar.auth.application.UserService;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import com.pickcar.auth.presentation.dto.response.EmployeeListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @PostMapping("/sign-up/admins")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void registerAdmin(@RequestBody UserInfoRequest request) {
//        userService.create(request, UserRole.SUPER_ADMIN);
//    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerEmployee(@RequestBody UserInfoRequest request) {
        userService.create(request);
    }

    @GetMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeListResponse> getEmployees() {
        List<EmployeeListResponse> responses = userService.getAllEmployees();
        return responses;
    }
}