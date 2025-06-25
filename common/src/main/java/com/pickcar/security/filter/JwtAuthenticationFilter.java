package com.pickcar.security.filter;

import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.security.jwt.TokenStatus;
import com.pickcar.security.principal.JwtUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final PathPatternParser patternParser = new PathPatternParser();

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/**",
            "/api/v1/token/refresh"
    );



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Request Path: {}", request.getRequestURI());

        if (EXCLUDE_URLS.stream()
                .map(patternParser::parse)
                .anyMatch(pathPattern -> pathPattern.matches(PathContainer.parsePath(request.getRequestURI())))) {
            log.info("Skipping JWT filter for Path");
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken 추출
        String accessToken = extractToken(request);
        log.info("AccessToken 추출: {}", accessToken);

        if(accessToken != null){
            TokenStatus status = jwtProvider.validateToken(accessToken);
            log.info("TokenStatus : {}", status);

            switch (status) {
                case VALID -> {
                    Claims claims = jwtProvider.parseToken(accessToken).getBody();
                    Long id = Long.valueOf(claims.getSubject());
                    String name = jwtProvider.validateAndGetClaim(claims,"name",String.class); //TODO: 값이 없을 경우의 예외처리 하기
                    String role = jwtProvider.validateAndGetClaim(claims,"role",String.class);

                    JwtUserDetails userDetails = new JwtUserDetails(id, name, role);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
                case EXPIRED -> {
                    // 클라이언트에 토큰 만료 안내 -> 클라이언트 측에서 AccessToken 재발급 요청 처리
                    sendUnauthorizedResponse(response,"ACCESS_TOKEN_EXPIRED");
                    return;
                }
                case INVALID_SIGNATURE, MALFORMED, INVALID -> {
                    // 위변조, 형식 오류, 기타 오류는 모두 인증 실패로 처리 -> 재로그인 요청
                    // TODO: 저장된 Access Token, Refresh Token 쿠키 및 메모리에서 모두 삭제
                    sendUnauthorizedResponse(response,"INVALID_ACCESS_TOKEN");
                    return;
                }
                default -> {
                    // 재로그인 요청
                    log.info("status : Unknown token error");
                    sendUnauthorizedResponse(response,"UNKNOWN_TOKEN_ERROR");
                    return;
                }
            }
        }else{
            // RefreshToken 추출
            String refreshToken = extractRefreshTokenFromCookie(request);
            log.info("RefreshToken 추출: {}", refreshToken);

            if (refreshToken != null && !refreshToken.isBlank()) {
                sendUnauthorizedResponse(response,"ACCESS_TOKEN_EXPIRED");
                return;
            } else {
                log.info("No token provided in request.");
                sendUnauthorizedResponse(response,"TOKEN_MISSING");
                return;
            }
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"" + message + "\"}");
            response.getWriter().flush();
        } catch (IOException e) {
            System.err.println("Failed to write unauthorized response: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        // Bearer 제거한 실제 토큰 값만 반환
        return (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}