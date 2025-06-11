package com.pickcar.auth.presentation.dto.request;

import com.pickcar.auth.domain.UserInfo;

public record UserInfoRequest(
        Long companyId,
        String email,
        String password,
        String name,
        String phoneNumber
){
    public UserInfo toInfo() {
        return new UserInfo(
                this.email,
                this.password,
                this.name,
                this.phoneNumber
        );
    }
}