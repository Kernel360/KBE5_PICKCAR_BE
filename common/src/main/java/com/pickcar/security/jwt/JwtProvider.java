package com.pickcar.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider { //FIX: JwtTokenProvider 로 변경하기

    @Value(value = "${jwt.secret-key}")
    private String jwtSecretKey;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    //ACCESS_TOKEN 생성
    public String createAccessToken(Long userId, String name, String role){
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("name", name)
                .claim("role", role)
                .claim("token_type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.ACCESS_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 테스트용 만료된 AccessToken 생성
    public String createExpiredAccessToken(Long userId, String name, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("name", name)
                .claim("role", role)
                .claim("token_type", "access")
                .setIssuedAt(new Date(now - 1000 * 60)) // 1분 전 발급
                .setExpiration(new Date(now - 1000 * 30)) // 30초 전에 만료됨
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //REFRESH_TOKEN 생성
    public String createRefreshToken(Long userId){
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("token_type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.REFRESH_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public TokenStatus validateToken(String token){
        if (token == null || token.trim().isEmpty()) {
            return TokenStatus.MISSING;
        }

        try{
            parseToken(token);
            return TokenStatus.VALID;
        }catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (SignatureException e) {
            return TokenStatus.INVALID_SIGNATURE;
        } catch (MalformedJwtException e) {
            return TokenStatus.MALFORMED;
        } catch (Exception e) {
            return TokenStatus.INVALID;
        }
    }

    public <T> T validateAndGetClaim(Claims claims,String key, Class<T> clazz){ //TODO: 분리하기
        return Optional.ofNullable(claims.get(key,clazz))
                .orElseThrow(() -> new IllegalArgumentException(
                        "JWT 클레임 '" + key + "'값이 없습니다."));
    }

    public LocalDateTime calculateExpiryDate(long validityMillis) { //TODO: 분리하기
        return LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis() + validityMillis).toInstant(),
                ZoneId.systemDefault()
        );
    }

    public Long extractUserId(HttpServletRequest request) {
        try {
            // 1. 쿠키에서 accessToken 우선 검색
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("accessToken")) {
                        String accessToken = cookie.getValue();
                        return extractUserIdFromToken(accessToken);
                    }
                }
            }

            // 2. Authorization 헤더에서 Bearer 토큰 검색
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7); // "Bearer " 이후 자르기
                return extractUserIdFromToken(token);
            }

            throw new IllegalArgumentException("인증 정보가 존재하지 않습니다");
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다", e);
        }
    }

    private Long extractUserIdFromToken(String token) {
        Jws<Claims> claimsJws = parseToken(token);
        return Long.valueOf(claimsJws.getBody().getSubject());
    }
}

