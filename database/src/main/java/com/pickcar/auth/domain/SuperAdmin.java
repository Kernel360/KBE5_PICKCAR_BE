package com.pickcar.auth.domain;

import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "super_admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuperAdmin extends BaseEntity { //TODO: super_admin 불필요, 제거하기

    @Embedded
    private UserInfo info;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.SUPER_ADMIN;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

}