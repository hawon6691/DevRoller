package com.devroller.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 응답 DTO
 */
@Getter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;  // 초 단위
    private Long userId;
    private String email;
    private String nickname;
}