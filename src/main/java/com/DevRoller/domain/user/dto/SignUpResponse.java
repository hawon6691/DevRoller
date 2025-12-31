package com.devroller.domain.user.dto;

import com.devroller.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 회원가입 응답 DTO
 */
@Getter
@Builder
public class SignUpResponse {

    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
    private Integer level;
    private LocalDateTime createdAt;

    public static SignUpResponse from(User user) {
        return SignUpResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .createdAt(user.getCreatedAt())
                .build();
    }
}