package com.pickcar.auth.presentation.dto.request;

public record UserInfoRequest(
        String email,
        String password,
        String name,
        String phoneNumber,
        Boolean isAdmin
){
}