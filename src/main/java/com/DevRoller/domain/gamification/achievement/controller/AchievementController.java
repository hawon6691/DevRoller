package com.devroller.domain.gamification.achievement.controller;

import com.devroller.domain.gamification.achievement.dto.AchievementResponse;
import com.devroller.domain.gamification.achievement.dto.UserAchievementResponse;
import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.gamification.achievement.service.AchievementService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 업적 API Controller
 */
@Tag(name = "Achievement", description = "업적 API")
@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @Operation(summary = "전체 업적 목록", description = "모든 업적 목록을 조회합니다. (히든 제외)")
    @GetMapping
    public ApiResponse<List<AchievementResponse>> getAllAchievements() {
        List<AchievementResponse> response = achievementService.getAllAchievements();
        return ApiResponse.success(response);
    }

    @Operation(summary = "타입별 업적", description = "특정 타입의 업적 목록을 조회합니다.")
    @GetMapping("/type/{type}")
    public ApiResponse<List<AchievementResponse>> getAchievementsByType(
            @PathVariable Achievement.AchievementType type) {
        List<AchievementResponse> response = achievementService.getAchievementsByType(type);
        return ApiResponse.success(response);
    }

    @Operation(summary = "내 업적 현황", description = "내가 진행중/완료한 업적 현황을 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<List<UserAchievementResponse>> getMyAchievements(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<UserAchievementResponse> response = achievementService.getUserAchievements(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "완료한 업적", description = "내가 달성한 업적 목록을 조회합니다.")
    @GetMapping("/me/completed")
    public ApiResponse<List<UserAchievementResponse>> getCompletedAchievements(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<UserAchievementResponse> response = achievementService.getCompletedAchievements(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "최근 달성 업적", description = "최근 달성한 업적 목록을 조회합니다.")
    @GetMapping("/me/recent")
    public ApiResponse<List<UserAchievementResponse>> getRecentAchievements(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "5") int limit) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<UserAchievementResponse> response = achievementService.getRecentAchievements(userId, limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "업적 통계", description = "내 업적 달성 통계를 조회합니다.")
    @GetMapping("/me/stats")
    public ApiResponse<Map<String, Long>> getAchievementStats(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        long completed = achievementService.getCompletedCount(userId);
        long total = achievementService.getAllAchievements().size();
        
        return ApiResponse.success(Map.of(
                "completed", completed,
                "total", total
        ));
    }
}