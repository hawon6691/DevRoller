package com.devroller.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 설정
 * - JWT 기반 인증 (OAuth2 Resource Server)
 * - Stateless 세션 관리
 * - CORS 설정
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // 인증 없이 접근 가능한 경로
    private static final String[] PUBLIC_URLS = {
            // 인증
            "/api/auth/**",
            // Swagger
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**",
            // Actuator (health만 공개)
            "/actuator/health",
            // H2 Console (개발용)
            "/h2-console/**",
            // 에러 페이지
            "/error"
    };

    // 인증 없이 GET 요청 가능한 경로
    private static final String[] PUBLIC_GET_URLS = {
            "/api/categories/**",
            "/api/ideas/**",
            "/api/tags/**",
            "/api/stats/public/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (JWT 사용)
            .csrf(csrf -> csrf.disable())
            
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // H2 Console용 Frame 옵션
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            
            // 세션 사용 안함 (Stateless)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 공개 URL
                .requestMatchers(PUBLIC_URLS).permitAll()
                // GET 요청 공개 URL
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_URLS).permitAll()
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            
            // OAuth2 Resource Server (JWT)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    /**
     * JWT 클레임을 Spring Security 권한으로 변환
     */
    @Bean
    public org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter() {
        var grantedAuthoritiesConverter = new org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        var jwtAuthenticationConverter = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        
        return jwtAuthenticationConverter;
    }

    /**
     * 비밀번호 암호화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용 Origin (개발 환경)
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:8080"
        ));
        
        // 허용 메소드
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // 허용 헤더
        configuration.setAllowedHeaders(List.of("*"));
        
        // 인증 정보 포함 허용
        configuration.setAllowCredentials(true);
        
        // 노출할 헤더
        configuration.setExposedHeaders(List.of("Authorization", "X-Total-Count"));
        
        // preflight 캐시 시간
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
