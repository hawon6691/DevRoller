package com.devroller.domain.gamification.achievement.dto;

import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 업적 응답 DTO
 */
@Getter
@Builder
public class UserAchievementResponse {

    private Long userAchievementId;
    private Long achievementId;
    private String code;
    private String name;
    private String description;
    private String icon;
    private String type;
    private Integer requiredValue;
    private Integer currentProgress;
    private Integer progressPercent;
    private Integer rewardExp;
    private Boolean isCompleted;
    private LocalDateTime achievedAt;

    public static UserAchievementResponse from(UserAchievement ua) {
        return UserAchievementResponse.builder()
                .userAchievementId(ua.getId())
                .achievementId(ua.getAchievement().getId())
                .code(ua.getAchievement().getCode())
                .name(ua.getAchievement().getName())
                .description(ua.getAchievement().getDescription())
                .icon(ua.getAchievement().getIcon())
                .type(ua.getAchievement().getType().name())
                .requiredValue(ua.getAchievement().getRequiredValue())
                .currentProgress(ua.getCurrentProgress())
                .progressPercent(ua.getProgressPercent())
                .rewardExp(ua.getAchievement().getRewardExp())
                .isCompleted(ua.getIsCompleted())
                .achievedAt(ua.getAchievedAt())
                .build();
    }
}