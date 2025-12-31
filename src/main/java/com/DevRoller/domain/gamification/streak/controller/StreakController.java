package com.devroller.domain.gamification.streak.controller;

import com.devroller.domain.gamification.streak.dto.StreakResponse;
import com.devroller.domain.gamification.streak.entity.Streak;
import com.devroller.domain.gamification.streak.service.StreakService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 스트릭 API Controller
 */
@Tag(name = "Streak", description = "스트릭(연속 기록) API")
@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class StreakController {

    private final StreakService streakService;

    @Operation(summary = "특정 기간 스트릭", description = "특정 기간의 스트릭 기록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<StreakResponse>> getStreaks(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<StreakResponse> response = streakService.getStreaks(userId, startDate, endDate);
        return ApiResponse.success(response);
    }

    @Operation(summary = "최근 스트릭", description = "최근 N일간의 스트릭 기록을 조회합니다.")
    @GetMapping("/recent")
    public ApiResponse<List<StreakResponse>> getRecentStreaks(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "30") int days) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<StreakResponse> response = streakService.getRecentStreaks(userId, days);
        return ApiResponse.success(response);
    }

    @Operation(summary = "월간 캘린더", description = "특정 월의 스트릭 캘린더 데이터를 조회합니다.")
    @GetMapping("/calendar")
    public ApiResponse<List<StreakResponse>> getMonthlyCalendar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam int year,
            @RequestParam int month) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<StreakResponse> response = streakService.getMonthlyCalendar(userId, year, month);
        return ApiResponse.success(response);
    }

    @Operation(summary = "현재 연속 스트릭", description = "현재 연속 활동 일수를 조회합니다.")
    @GetMapping("/current")
    public ApiResponse<Integer> getCurrentStreak(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        int currentStreak = streakService.calculateCurrentStreak(userId);
        return ApiResponse.success(currentStreak);
    }

    @Operation(summary = "오늘 활동 여부", description = "오늘 활동 기록이 있는지 확인합니다.")
    @GetMapping("/today")
    public ApiResponse<Boolean> hasActivityToday(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        boolean hasActivity = streakService.hasActivityToday(userId);
        return ApiResponse.success(hasActivity);
    }

    @Operation(summary = "스트릭 통계", description = "스트릭 관련 통계를 조회합니다.")
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getStreakStats(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        
        int currentStreak = streakService.calculateCurrentStreak(userId);
        long totalActivityDays = streakService.getTotalActivityDays(userId);
        long totalPicks = streakService.getTotalActivityCount(userId, Streak.ActivityType.PICK);
        long totalCompletes = streakService.getTotalActivityCount(userId, Streak.ActivityType.COMPLETE);
        long totalReviews = streakService.getTotalActivityCount(userId, Streak.ActivityType.REVIEW);
        
        return ApiResponse.success(Map.of(
                "currentStreak", currentStreak,
                "totalActivityDays", totalActivityDays,
                "totalPicks", totalPicks,
                "totalCompletes", totalCompletes,
                "totalReviews", totalReviews
        ));
    }
}