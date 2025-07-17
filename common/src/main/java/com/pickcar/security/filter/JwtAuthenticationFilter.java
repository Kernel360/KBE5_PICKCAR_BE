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

    //FIX: 변수명 수정 및 주소 관리 방법 수정
//    public static final String TOKEN_REFRESH_URI = "/api/v1/token/refresh";
    private static final List<String> EXCLUDE_URLS_REFRESH = List.of(
            "/api/v1/token/refresh",
            "/api/v1/token/refresh-expired"
    );
    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/sign-up",
            "/api/v1/auth/login",
            "/api/v1/auth/logout",
            "/api/v1/sse/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (EXCLUDE_URLS.stream()
                .map(patternParser::parse)
                .anyMatch(pathPattern -> pathPattern.matches(PathContainer.parsePath(request.getRequestURI())))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (EXCLUDE_URLS_REFRESH.stream()
                .map(patternParser::parse)
                .anyMatch(pathPattern -> pathPattern.matches(PathContainer.parsePath(request.getRequestURI())))) {
            String refreshToken = extractRefreshTokenFromCookie(request);

            TokenStatus refreshTokenStatus = jwtProvider.validateToken(refreshToken);

            if (refreshTokenStatus == TokenStatus.VALID) {
                // RefreshToken을 request에 저장
                request.setAttribute("refreshToken", refreshToken);
                filterChain.doFilter(request, response);
                return;
            }

            sendUnauthorizedResponse(response, refreshTokenStatus);
            return;
        }

        // AccessToken 추출
        String accessToken = extractAccessToken(request);

        TokenStatus accessTokenStatus = jwtProvider.validateToken(accessToken);

        if(accessTokenStatus == TokenStatus.VALID){
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
        sendUnauthorizedResponse(response,accessTokenStatus);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, TokenStatus status) {
        try {
            response.setStatus(status.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            String timeStamp = java.time.ZonedDateTime.now().toString();

            String json = String.format("""
                {
                  "responseInfo": {
                    "isSuccess": false,
                    "statusCode": %d,
                    "timeStamp": "%s"
                  },
                  "errorReason": {
                    "errorCode": "%s",
                    "reason": "%s"
                  }
                }
                """,
                    status.getHttpStatus().value(),
                    timeStamp,
                    status.getErrorCode(),
                    status.getDescription()
            );

            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) {
            System.err.println("Failed to write unauthorized response: " + e.getMessage());
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        // Bearer 제거한 실제 토큰 값만 반환, 아니면 null 반환
        return (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
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