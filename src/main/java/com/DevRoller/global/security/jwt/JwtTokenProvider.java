package com.devroller.global.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * JWT 토큰 생성 Provider
 * - Access Token 생성
 * - Refresh Token 생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    /**
     * Access Token 생성
     * 
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param roles 사용자 권한 목록
     * @return JWT Access Token
     */
    public String generateAccessToken(Long userId, String email, List<String> roles) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getExpiration(), ChronoUnit.MILLIS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("roles", roles)
                .claim("type", "access")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
        log.debug("Access token generated for user: {}", userId);
        return token;
    }

    /**
     * Refresh Token 생성
     * 
     * @param userId 사용자 ID
     * @return JWT Refresh Token
     */
    public String generateRefreshToken(Long userId) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
        log.debug("Refresh token generated for user: {}", userId);
        return token;
    }

    /**
     * Access Token 만료 시간 (초 단위)
     */
    public long getAccessTokenExpirationSeconds() {
        return jwtProperties.getExpiration() / 1000;
    }

    /**
     * Refresh Token 만료 시간 (초 단위)
     */
    public long getRefreshTokenExpirationSeconds() {
        return jwtProperties.getRefreshExpiration() / 1000;
    }
}
