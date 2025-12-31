package com.devroller.domain.gamification.achievement.dto;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import lombok.Builder;
import lombok.Getter;

/**
 * 업적 응답 DTO
 */
@Getter
@Builder
public class AchievementResponse {

    private Long achievementId;
    private String code;
    private String name;
    private String description;
    private String icon;
    private String type;
    private Integer requiredValue;
    private Integer rewardExp;
    private Boolean isHidden;

    public static AchievementResponse from(Achievement achievement) {
        return AchievementResponse.builder()
                .achievementId(achievement.getId())
                .code(achievement.getCode())
                .name(achievement.getName())
                .description(achievement.getDescription())
                .icon(achievement.getIcon())
                .type(achievement.getType().name())
                .requiredValue(achievement.getRequiredValue())
                .rewardExp(achievement.getRewardExp())
                .isHidden(achievement.getIsHidden())
                .build();
    }
}