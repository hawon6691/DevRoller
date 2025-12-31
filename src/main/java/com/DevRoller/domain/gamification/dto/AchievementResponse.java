package com.devroller.domain.gamification.dto;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AchievementResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String icon;
    private String type;
    private Integer requiredValue;
    private Integer rewardExp;
    private Boolean isHidden;
    
    // 사용자 진행 정보
    private Integer currentProgress;
    private Boolean isCompleted;
    private LocalDateTime achievedAt;
    private Integer progressPercent;

    public static AchievementResponse from(Achievement achievement, UserAchievement userAchievement) {
        AchievementResponseBuilder builder = AchievementResponse.builder()
                .id(achievement.getId())
                .code(achievement.getCode())
                .name(achievement.getName())
                .description(achievement.getDescription())
                .icon(achievement.getIcon())
                .type(achievement.getType().name())
                .requiredValue(achievement.getRequiredValue())
                .rewardExp(achievement.getRewardExp())
                .isHidden(achievement.getIsHidden());

        if (userAchievement != null) {
            builder.currentProgress(userAchievement.getCurrentProgress())
                    .isCompleted(userAchievement.getIsCompleted())
                    .achievedAt(userAchievement.getAchievedAt())
                    .progressPercent(userAchievement.getProgressPercent());
        } else {
            builder.currentProgress(0)
                    .isCompleted(false)
                    .progressPercent(0);
        }

        return builder.build();
    }
}
