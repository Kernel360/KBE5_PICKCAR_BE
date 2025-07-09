package com.pickcar.auth.presentation.dto.response;

import com.pickcar.auth.domain.User;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.domain.UserStatus;

public record UnAllocatedEmployeeResponse(
        Long userId,
        String name,
        UserStatus status,
        UserRole role,
        String email
) {

    public static UnAllocatedEmployeeResponse from (User user) {
        return new UnAllocatedEmployeeResponse(
                user.getId(),
                user.getInfo().getName(),
                user.getStatus(),
                user.getRole(),
                user.getInfo().getEmail()
        );
    }
}
