package com.pickcar.auth.presentation.dto.request;

public record AuthRequest(
        String email,
        String password
){
}