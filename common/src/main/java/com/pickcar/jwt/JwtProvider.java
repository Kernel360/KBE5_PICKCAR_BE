package com.pickcar.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    //@Value("${jwt.secret}")
    private String secret = "ZsR872u9NukGnsjbY5olgyIPZTErn82NETmxjpozaS4=";

    private static final long EXPIRATION = 1000 * 60 * 60L; // 1시간

    //토큰 생성
    public String generateToken(Long Id, Long companyId, String role) {
        return Jwts.builder()
                .setSubject(Id.toString())
                .claim("role", role)
                .claim("companyId", companyId) // super-admin은 0
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //토큰 분해
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
