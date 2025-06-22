package com.pickcar.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider { //FIX: JwtTokenProvider 로 변경하기

    //ACCESS_TOKEN 생성
    public String createAccessToken(Long userId, String name, String role){
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("name", name)
                .claim("role", role)
                .claim("token_type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SECRET_KEY)
                .compact();
    }

    //REFRESH_TOKEN 생성
    public String createRefreshToken(Long userId){
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("token_type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SECRET_KEY)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstants.JWT_SECRET_KEY)
                .parseClaimsJws(token);
    }

    public TokenStatus validateToken(String token){
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

    public <T> T validateAndGetClaim(Claims claims,String key, Class<T> clazz){
        return Optional.ofNullable(claims.get(key,clazz))
                .orElseThrow(() -> new IllegalArgumentException(
                        "JWT 클레임 '" + key + "'값이 없습니다."));
    }

    public LocalDateTime calculateExpiryDate(long validityMillis) {
        return LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis() + validityMillis).toInstant(),
                ZoneId.systemDefault()
        );
    }
}
