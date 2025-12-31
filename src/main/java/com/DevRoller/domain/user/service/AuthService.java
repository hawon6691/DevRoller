package com.devroller.domain.user.service;

import com.devroller.domain.user.dto.LoginRequest;
import com.devroller.domain.user.dto.LoginResponse;
import com.devroller.domain.user.dto.SignUpRequest;
import com.devroller.domain.user.dto.SignUpResponse;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import com.devroller.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 인증 서비스
 * - 회원가입
 * - 로그인
 * - 토큰 갱신
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 체크
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .build();

        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getEmail());

        return SignUpResponse.from(savedUser);
    }

    /**
     * 로그인
     */
    public LoginResponse login(LoginRequest request) {
        // 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 계정 상태 확인
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "비활성화된 계정입니다.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        List<String> roles = List.of(user.getRole().name());
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        log.info("User logged in: {}", user.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationSeconds())
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 토큰 갱신
     */
    public LoginResponse refreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "비활성화된 계정입니다.");
        }

        List<String> roles = List.of(user.getRole().name());
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationSeconds())
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 이메일 중복 확인
     */
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 확인
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}