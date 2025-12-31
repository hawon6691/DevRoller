package com.devroller.domain.gamification.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 웹소설/웹툰 스타일 스테이터스 창 응답
 */
@Getter
@Builder
public class StatusWindowResponse {

    private String nickname;
    private Integer level;
    private Integer experience;
    private Integer experienceToNextLevel;
    private String equippedTitle;
    private Integer totalCompleted;
    private Integer currentStreak;
    private Integer maxStreak;
    private Integer completedAchievements;
    private Integer totalAchievements;
    private Integer ownedTitles;
    private Integer totalTitles;

    /**
     * 경험치 진행률 (0-100)
     */
    public int getExpProgressPercent() {
        if (experienceToNextLevel == 0) return 100;
        return Math.min(100, (experience * 100) / experienceToNextLevel);
    }

    /**
     * 업적 진행률 (0-100)
     */
    public int getAchievementProgressPercent() {
        if (totalAchievements == 0) return 0;
        return (completedAchievements * 100) / totalAchievements;
    }
}
