package com.pickcar.auth.presentation;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.presentation.dto.response.AccessTokenResponse;
import com.pickcar.auth.presentation.dto.response.AuthResponse;
import com.pickcar.security.jwt.JwtConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

    private final AuthService authService;

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse reissueRefreshToken(HttpServletRequest request, HttpServletResponse response){
        log.info("Issued new AccessToken for refreshToken");
        String refreshToken = extractRefreshTokenFromCookie(request);
        AuthResponse authResponse = authService.reissueTokens(refreshToken);
        addRefreshTokenToCookie(response, authResponse.refreshToken());
        return new AccessTokenResponse(authResponse.accessToken());
    }

    private void addRefreshTokenToCookie(HttpServletResponse response,String refreshToken){//TODO: 자주 사용되서 Util로 빼기
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (JwtConstants.REFRESH_TOKEN_VALIDITY / 1000));
//        cookie.setSecure(true); // HTTPS 환경에서만 쿠키가 전송되도록
        response.addCookie(cookie);
    } //TODO: 쿠키 설정 실패 예외처리 추가

    private String extractRefreshTokenFromCookie(HttpServletRequest request) { //TODO: 자주 사용되서 Util로 빼기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null; // 쿠키에 refreshToken이 없으면 null 반환
    }
}
