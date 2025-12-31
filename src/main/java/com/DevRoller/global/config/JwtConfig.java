package com.devroller.global.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * JWT Encoder / Decoder 설정
 * RSA 키 기반 비대칭 암호화 사용
 */
@Slf4j
@Configuration
public class JwtConfig {

    @Value("${jwt.private-key}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key}")
    private Resource publicKeyResource;

    /**
     * RSA Public Key 로드
     */
    @Bean
    public RSAPublicKey rsaPublicKey() throws Exception {
        String publicKeyPEM = readKey(publicKeyResource);
        publicKeyPEM = publicKeyPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA Private Key 로드
     */
    @Bean
    public RSAPrivateKey rsaPrivateKey() throws Exception {
        String privateKeyPEM = readKey(privateKeyResource);
        privateKeyPEM = privateKeyPEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * JWT Decoder (토큰 검증용)
     */
    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    /**
     * JWT Encoder (토큰 생성용)
     */
    @Bean
    public JwtEncoder jwtEncoder(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        JWK jwk = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .build();
        
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * 키 파일 읽기
     */
    private String readKey(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
