package com.pickcar.auth.utils;

import com.pickcar.security.jwt.JwtConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenUtils {

    public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setAttribute("SameSite", "Strict"); //TODO: 환경변수 설정
//        cookie.setDomain("pickcar.online");
//        cookie.setSecure(true); // 운영 환경에서는 주석 해제 필요 //TODO: 환경변수 설정

        if (refreshToken == null) {
            cookie.setMaxAge(0); // 삭제
        } else {
            cookie.setMaxAge((int) (JwtConstants.REFRESH_TOKEN_VALIDITY / 1000));
        }
        response.addCookie(cookie);
    }

    public static String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}