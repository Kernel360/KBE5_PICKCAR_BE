package com.pickcar.auth.presentation.dto.request;

import jakarta.validation.constraints.*;

public record UserInfoRequest(
        String email,
        String password,
        String name,
        String phoneNumber,
        Boolean isAdmin
){
}