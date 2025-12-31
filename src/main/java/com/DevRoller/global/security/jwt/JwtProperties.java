package com.devroller.global.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 설정 프로퍼티
 * application.yml의 jwt 설정을 바인딩
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * RSA Private Key 경로 (토큰 서명용)
     */
    private String privateKey = "classpath:keys/private.pem";

    /**
     * RSA Public Key 경로 (토큰 검증용)
     */
    private String publicKey = "classpath:keys/public.pem";

    /**
     * Access Token 만료 시간 (ms)
     * 기본값: 24시간
     */
    private long expiration = 86400000;

    /**
     * Refresh Token 만료 시간 (ms)
     * 기본값: 7일
     */
    private long refreshExpiration = 604800000;

    /**
     * 토큰 발급자
     */
    private String issuer = "devroller";
}
