package com.devroller.domain.gamification.streak.dto;

import com.devroller.domain.gamification.streak.entity.Streak;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 스트릭 응답 DTO
 */
@Getter
@Builder
public class StreakResponse {

    private Long streakId;
    private LocalDate activityDate;
    private String activityType;
    private Integer count;

    public static StreakResponse from(Streak streak) {
        return StreakResponse.builder()
                .streakId(streak.getId())
                .activityDate(streak.getActivityDate())
                .activityType(streak.getActivityType().name())
                .count(streak.getCount())
                .build();
    }
}