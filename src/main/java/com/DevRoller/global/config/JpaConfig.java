package com.devroller.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * JPA Auditing 설정
 * - createdAt, updatedAt 자동 설정
 * - createdBy, updatedBy 자동 설정 (옵션)
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    /**
     * 현재 인증된 사용자 정보 제공
     * @CreatedBy, @LastModifiedBy 에서 사용
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("SYSTEM");
            }
            
            Object principal = authentication.getPrincipal();
            
            // JWT 토큰에서 subject(사용자 ID) 추출
            if (principal instanceof Jwt jwt) {
                return Optional.ofNullable(jwt.getSubject());
            }
            
            // 익명 사용자
            if ("anonymousUser".equals(principal)) {
                return Optional.of("ANONYMOUS");
            }
            
            return Optional.of(principal.toString());
        };
    }
}
