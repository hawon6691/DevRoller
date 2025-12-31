package com.devroller.domain.user.dto;

import com.devroller.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private Integer level;
    private Integer experience;
    private Integer experienceToNextLevel;
    private Integer totalCompleted;
    private Integer currentStreak;
    private Integer maxStreak;
    private String role;
    private Long equippedTitleId;
    private String equippedTitleName;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .experience(user.getExperience())
                .experienceToNextLevel(user.getLevel() * 100)
                .totalCompleted(user.getTotalCompleted())
                .currentStreak(user.getCurrentStreak())
                .maxStreak(user.getMaxStreak())
                .role(user.getRole().name())
                .equippedTitleId(user.getEquippedTitleId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static UserResponse withTitle(User user, String titleName) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .experience(user.getExperience())
                .experienceToNextLevel(user.getLevel() * 100)
                .totalCompleted(user.getTotalCompleted())
                .currentStreak(user.getCurrentStreak())
                .maxStreak(user.getMaxStreak())
                .role(user.getRole().name())
                .equippedTitleId(user.getEquippedTitleId())
                .equippedTitleName(titleName)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
