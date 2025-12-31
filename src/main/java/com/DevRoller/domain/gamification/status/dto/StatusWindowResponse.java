package com.devroller.domain.gamification.status.dto;

import com.devroller.domain.gamification.achievement.dto.UserAchievementResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 사용자 상태창 응답 DTO
 * 웹소설/웹툰 스타일의 상태창 정보
 */
@Getter
@Builder
public class StatusWindowResponse {

    // 기본 정보
    private Long userId;
    private String nickname;
    private String profileImage;

    // 레벨 & 경험치
    private Integer level;
    private Integer experience;
    private Integer experienceToNextLevel;

    // 칭호
    private String equippedTitleName;
    private String equippedTitleRarity;

    // 스트릭
    private Integer currentStreak;
    private Integer maxStreak;
    private Long totalActivityDays;

    // 프로젝트 통계
    private Integer totalCompleted;
    private Long projectsInProgress;
    private Long projectsAbandoned;
    private Long totalPicks;

    // 수집 통계
    private Long completedAchievements;
    private Long ownedTitles;

    // 최근 업적
    private List<UserAchievementResponse> recentAchievements;
}