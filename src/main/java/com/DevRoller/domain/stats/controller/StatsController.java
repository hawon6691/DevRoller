package com.devroller.domain.stats.controller;

import com.devroller.domain.stats.dto.PopularIdeasResponse;
import com.devroller.domain.stats.dto.UserRankingResponse;
import com.devroller.domain.stats.dto.UserStatsResponse;
import com.devroller.domain.stats.service.StatsService;
import com.devroller.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * 인기 주제 TOP N 조회
     * GET /api/v1/stats/popular?limit=10
     */
    @GetMapping("/popular")
    public ApiResponse<PopularIdeasResponse> getPopularIdeas(
            @RequestParam(defaultValue = "10") int limit) {
        PopularIdeasResponse response = statsService.getPopularIdeas(limit);
        return ApiResponse.success(response);
    }

    /**
     * 사용자 랭킹 조회
     * GET /api/v1/stats/ranking?type=level&limit=10
     * type: level, completed, streak
     */
    @GetMapping("/ranking")
    public ApiResponse<UserRankingResponse> getUserRanking(
            @RequestParam(defaultValue = "level") String type,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        Long currentUserId = authentication != null
                ? Long.parseLong(authentication.getName())
                : null;
        UserRankingResponse response = statsService.getUserRanking(type, limit, currentUserId);
        return ApiResponse.success(response);
    }

    /**
     * 내 통계 조회
     * GET /api/v1/stats/my
     */
    @GetMapping("/my")
    public ApiResponse<UserStatsResponse> getMyStats(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserStatsResponse response = statsService.getUserStats(userId);
        return ApiResponse.success(response);
    }

    /**
     * 특정 사용자 통계 조회
     * GET /api/v1/stats/users/{userId}
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<UserStatsResponse> getUserStats(@PathVariable Long userId) {
        UserStatsResponse response = statsService.getUserStats(userId);
        return ApiResponse.success(response);
    }
}
