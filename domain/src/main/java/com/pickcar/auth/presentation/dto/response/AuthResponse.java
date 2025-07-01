package com.pickcar.auth.presentation.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
){
}
