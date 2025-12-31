package com.devroller.domain.auth.service;

import com.devroller.domain.auth.dto.LoginRequest;
import com.devroller.domain.auth.dto.SignupRequest;
import com.devroller.domain.auth.dto.TokenResponse;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import com.devroller.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtDecoder jwtDecoder;

    /**
     * 회원가입
     */
    @Transactional
    public TokenResponse signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 체크
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        // 토큰 발급
        return generateTokens(user);
    }

    /**
     * 로그인
     */
    public TokenResponse login(LoginRequest request) {
        // 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 계정 상태 확인
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        log.info("User logged in: {}", user.getEmail());

        // 토큰 발급
        return generateTokens(user);
    }

    /**
     * 토큰 갱신
     */
    public TokenResponse refresh(String refreshToken) {
        try {
            // 리프레시 토큰 검증
            Jwt jwt = jwtDecoder.decode(refreshToken);
            
            // 리프레시 토큰인지 확인
            String tokenType = jwt.getClaim("type");
            if (!"refresh".equals(tokenType)) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }

            // 사용자 조회
            Long userId = Long.parseLong(jwt.getSubject());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 계정 상태 확인
            if (user.getStatus() != User.UserStatus.ACTIVE) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED);
            }

            // 새 토큰 발급
            return generateTokens(user);
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 토큰 생성
     */
    private TokenResponse generateTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        
        return TokenResponse.of(
                accessToken, 
                refreshToken, 
                jwtTokenProvider.getAccessTokenExpiration()
        );
    }
}
