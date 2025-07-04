package com.pickcar.auth.presentation.dto.response;

import com.pickcar.auth.domain.User;
import com.pickcar.auth.domain.UserInfo;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.domain.UserStatus;

public record EmployeeListResponse(
        Long userId,
        String name,
        UserStatus status,
        UserRole role,
        String email
) {

    public static EmployeeListResponse from(User user) {
        UserInfo info = user.getInfo();
        return new EmployeeListResponse(user.getId(), info.getName(), user.getStatus(), user.getRole(),
                info.getEmail());
    }
}
