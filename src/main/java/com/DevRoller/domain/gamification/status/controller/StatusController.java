package com.devroller.domain.gamification.status.controller;

import com.devroller.domain.gamification.achievement.dto.UserAchievementResponse;
import com.devroller.domain.gamification.achievement.service.AchievementService;
import com.devroller.domain.gamification.status.dto.StatusWindowResponse;
import com.devroller.domain.gamification.streak.service.StreakService;
import com.devroller.domain.gamification.title.dto.UserTitleResponse;
import com.devroller.domain.gamification.title.service.TitleService;
import com.devroller.domain.idea.service.UserIdeaService;
import com.devroller.domain.pick.service.PickService;
import com.devroller.domain.user.dto.UserProfileResponse;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 상태창 API Controller
 * 웹소설/웹툰 스타일의 상태창 정보 제공
 */
@Tag(name = "Status", description = "사용자 상태창 API")
@RestController
@RequestMapping("/api/status")
@RequiredArgsConstructor
public class StatusController {

    private final UserService userService;
    private final AchievementService achievementService;
    private final TitleService titleService;
    private final StreakService streakService;
    private final UserIdeaService userIdeaService;
    private final PickService pickService;

    @Operation(summary = "내 상태창", description = "웹소설 스타일의 사용자 상태창 정보를 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<StatusWindowResponse> getMyStatus(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        StatusWindowResponse response = buildStatusWindow(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "사용자 상태창", description = "특정 사용자의 상태창 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<StatusWindowResponse> getUserStatus(@PathVariable Long userId) {
        StatusWindowResponse response = buildStatusWindow(userId);
        return ApiResponse.success(response);
    }

    private StatusWindowResponse buildStatusWindow(Long userId) {
        // 기본 프로필
        UserProfileResponse profile = userService.getProfile(userId);

        // 장착된 칭호
        Optional<UserTitleResponse> equippedTitle = titleService.getEquippedTitle(userId);

        // 최근 업적 (3개)
        List<UserAchievementResponse> recentAchievements = achievementService.getRecentAchievements(userId, 3);

        // 통계
        int currentStreak = streakService.calculateCurrentStreak(userId);
        long totalActivityDays = streakService.getTotalActivityDays(userId);
        long completedAchievements = achievementService.getCompletedCount(userId);
        long ownedTitles = titleService.getTitleCount(userId);
        long totalPicks = pickService.getTotalPickCount(userId);
        UserIdeaService.ProjectStats projectStats = userIdeaService.getProjectStats(userId);

        return StatusWindowResponse.builder()
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .profileImage(profile.getProfileImage())
                .level(profile.getLevel())
                .experience(profile.getExperience())
                .experienceToNextLevel(profile.getExperienceToNextLevel())
                .equippedTitleName(equippedTitle.map(UserTitleResponse::getName).orElse(null))
                .equippedTitleRarity(equippedTitle.map(UserTitleResponse::getRarity).orElse(null))
                .currentStreak(currentStreak)
                .maxStreak(profile.getMaxStreak())
                .totalActivityDays(totalActivityDays)
                .totalCompleted(profile.getTotalCompleted())
                .projectsInProgress(projectStats.inProgress())
                .projectsAbandoned(projectStats.abandoned())
                .totalPicks(totalPicks)
                .completedAchievements(completedAchievements)
                .ownedTitles(ownedTitles)
                .recentAchievements(recentAchievements)
                .build();
    }
}