package com.pickcar.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtConstants {
    public static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000L;

    public static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L;

    //@Value("${jwt.secret}") TODO: secret 변수처리
    public static final String JWT_SECRET_KEY = "ZsR872u9NukGnsjbY5olgyIPZTErn82NETmxjpozaS4=";

//    public static final String TOKEN_PREFIX = "Bearer ";

//    public static final String HEADER_STRING = "Authorization";

}
