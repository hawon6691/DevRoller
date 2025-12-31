package com.devroller.domain.user.dto;

import com.devroller.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 프로필 응답 DTO
 */
@Getter
@Builder
public class UserProfileResponse {

    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
    private Integer level;
    private Integer experience;
    private Integer experienceToNextLevel;  // 다음 레벨까지 필요 경험치
    private Integer totalCompleted;
    private Integer currentStreak;
    private Integer maxStreak;
    private Long equippedTitleId;
    private String equippedTitleName;  // 장착된 칭호명 (별도 조회 필요)
    private LocalDateTime createdAt;

    public static UserProfileResponse from(User user) {
        int currentLevel = user.getLevel();
        int requiredExp = currentLevel * 100;  // 다음 레벨 필요 경험치
        int expToNext = requiredExp - user.getExperience();

        return UserProfileResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .experience(user.getExperience())
                .experienceToNextLevel(Math.max(0, expToNext))
                .totalCompleted(user.getTotalCompleted())
                .currentStreak(user.getCurrentStreak())
                .maxStreak(user.getMaxStreak())
                .equippedTitleId(user.getEquippedTitleId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 칭호명 설정 (별도 조회 후)
    public UserProfileResponse withTitleName(String titleName) {
        return UserProfileResponse.builder()
                .userId(this.userId)
                .email(this.email)
                .nickname(this.nickname)
                .profileImage(this.profileImage)
                .level(this.level)
                .experience(this.experience)
                .experienceToNextLevel(this.experienceToNextLevel)
                .totalCompleted(this.totalCompleted)
                .currentStreak(this.currentStreak)
                .maxStreak(this.maxStreak)
                .equippedTitleId(this.equippedTitleId)
                .equippedTitleName(titleName)
                .createdAt(this.createdAt)
                .build();
    }
}