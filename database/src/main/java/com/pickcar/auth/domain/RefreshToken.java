package com.pickcar.auth.domain;

import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Builder
    public RefreshToken(Long userId, String token, LocalDateTime expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static RefreshToken create(Long userId, String token, LocalDateTime expiryDate) {
//        LocalDateTime expiryDate = LocalDateTime.ofInstant(
//                new Date(System.currentTimeMillis() + validityMillis).toInstant(),
//                ZoneId.systemDefault()
//        ); //TODO: 분리하기
        return new RefreshToken(
                userId,
                token,
                expiryDate
        );
    }

    public void update(String newToken, LocalDateTime newExpiryDate) {
        this.token = newToken;
        this.expiryDate = newExpiryDate;
    }
}