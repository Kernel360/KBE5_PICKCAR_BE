package com.pickcar.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtConstants {
    public static final long ACCESS_TOKEN_VALIDITY = 3000 * 60 * 1000L;

    public static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L;

//    public static final String TOKEN_PREFIX = "Bearer ";

//    public static final String HEADER_STRING = "Authorization";

}
