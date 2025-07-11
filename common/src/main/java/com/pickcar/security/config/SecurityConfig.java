package com.pickcar.security.config;

import com.pickcar.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value(value = "${web.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF비활성화, 커스텀 CORS설정, 폼 로그인 비활성화, Basic 인증 비활성화, 인가 설정(모든 요청 허용중)
        return http
                //보안 설정
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                //인증 설정
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                //JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                //권한 설정
                .authorizeHttpRequests(auth -> auth
                        //SUPER_ADMIN도 접근 가능하게 해야 하나?
//                        .requestMatchers("/api/v1/vehicles/allocation/**", "/api/v1/cycle/**", "/api/v1/event/**").hasAnyRole("EMPLOYEE")
//                        .requestMatchers(
//                                "/api/v1/trackingcars",
//                                "/api/v1/sse/**",
//                                "/api/v1/reservation/vehicles",
//                                "/api/v1/history/**",
//                                "/api/v1/companies/**",
//                                "/api/v1/auth/employees"
//                        ).hasRole("SUPER_ADMIN")
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern(allowedOrigins);
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        );
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 쿠키나 인증 정보 포함된 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration); //'/api/v1/**'에만 CORS 설정 적용
        return source;
    }
}
