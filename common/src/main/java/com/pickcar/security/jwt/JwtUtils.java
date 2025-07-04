package com.pickcar.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtils {
    public static LocalDateTime calculateExpiryDate(long validityMillis) {
        return LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis() + validityMillis).toInstant(),
                ZoneId.systemDefault()
        );
    }
}