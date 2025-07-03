package com.pickcar.security.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider { //FIX: JwtTokenProvider 로 변경하기

    @Value(value = "${custom.jwt.secret}")
    private String jwtSecretKey;

    //ACCESS_TOKEN 생성
    public String createAccessToken(Long userId, String name, String role){
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("name", name)
                .claim("role", role)
                .claim("token_type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    //REFRESH_TOKEN 생성
    public String createRefreshToken(Long userId){
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("token_type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token);
    }

    public TokenStatus validateToken(String token){ //TODO: 분리하기
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

    public Long extractUserId(HttpServletRequest servletRequest) {

        for (Cookie cookie : servletRequest.getCookies()) {
            if(cookie.getName().equals("accessToken")) {
                String accessToken = cookie.getValue();
                Jws<Claims> claimsJws = parseToken(accessToken);
                return Long.valueOf(claimsJws.getBody().getSubject());
            }
        }

        throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다");
    }
}
