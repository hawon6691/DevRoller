package com.devroller.domain.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse {

    private ProjectStats projectStats;
    private List<CategoryStat> categoryStats;
    private StreakInfo streakInfo;
    private LevelInfo levelInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectStats {
        private Integer total;
        private Integer completed;
        private Integer inProgress;
        private Double completionRate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStat {
        private String categoryName;
        private Integer count;
        private Double percentage;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StreakInfo {
        private Integer current;
        private Integer max;
        private String lastActivityDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LevelInfo {
        private Integer currentLevel;
        private Integer currentExp;
        private Integer requiredExp;
        private Double progress;
    }

    public static UserStatsResponse of(
            ProjectStats projectStats,
            List<CategoryStat> categoryStats,
            StreakInfo streakInfo,
            LevelInfo levelInfo) {
        return UserStatsResponse.builder()
                .projectStats(projectStats)
                .categoryStats(categoryStats)
                .streakInfo(streakInfo)
                .levelInfo(levelInfo)
                .build();
    }
}
