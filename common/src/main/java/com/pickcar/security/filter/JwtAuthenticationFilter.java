package com.pickcar.security.filter;

import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.security.jwt.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //1. request 헤더에서 JWT 토큰 추출
        String token = extractToken(request);
        if (token != null) {
            //2. 토큰에 담긴 정보 추출
            jwtProvider.validateToken(token);
            Claims claims = jwtProvider.parseToken(token).getBody();
            Long id = Long.valueOf(claims.getSubject());
            String name = jwtProvider.validateAndGetClaim(claims,"name",String.class); //TODO: 값이 없을 경우의 예외처리 하기
            String role = jwtProvider.validateAndGetClaim(claims,"role",String.class);
            String tokenType = jwtProvider.validateAndGetClaim(claims,"token_type",String.class);

            //3. 로그인한 사용자의 ID와 role을 담은 객체 생성
            UserPrincipal principal = new UserPrincipal(id, name, role);

            //4. 인증 객체 생성 (setAuthenticated 인증 성공 여부 true)
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            //5. SecurityContextHolder에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        //6. 다음 필터 또는 컨트롤러로 요청 넘기기
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        // Bearer 제거한 실제 토큰 값만 반환
        return (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
    }
}