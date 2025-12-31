package com.devroller.domain.gamification.title.controller;

import com.devroller.domain.gamification.title.dto.TitleResponse;
import com.devroller.domain.gamification.title.dto.UserTitleResponse;
import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.service.TitleService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 칭호 API Controller
 */
@Tag(name = "Title", description = "칭호 API")
@RestController
@RequestMapping("/api/titles")
@RequiredArgsConstructor
public class TitleController {

    private final TitleService titleService;

    @Operation(summary = "전체 칭호 목록", description = "모든 칭호 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<TitleResponse>> getAllTitles() {
        List<TitleResponse> response = titleService.getAllTitles();
        return ApiResponse.success(response);
    }

    @Operation(summary = "타입별 칭호", description = "특정 타입의 칭호 목록을 조회합니다.")
    @GetMapping("/type/{type}")
    public ApiResponse<List<TitleResponse>> getTitlesByType(@PathVariable Title.TitleType type) {
        List<TitleResponse> response = titleService.getTitlesByType(type);
        return ApiResponse.success(response);
    }

    @Operation(summary = "내 칭호 목록", description = "내가 보유한 칭호 목록을 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<List<UserTitleResponse>> getMyTitles(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<UserTitleResponse> response = titleService.getUserTitles(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "장착된 칭호", description = "현재 장착된 칭호를 조회합니다.")
    @GetMapping("/me/equipped")
    public ApiResponse<UserTitleResponse> getEquippedTitle(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        Optional<UserTitleResponse> response = titleService.getEquippedTitle(userId);
        return response.map(ApiResponse::success)
                .orElse(ApiResponse.success("장착된 칭호가 없습니다.", null));
    }

    @Operation(summary = "칭호 장착", description = "칭호를 장착합니다.")
    @PostMapping("/{titleId}/equip")
    public ApiResponse<Void> equipTitle(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long titleId) {
        Long userId = Long.parseLong(jwt.getSubject());
        titleService.equipTitle(userId, titleId);
        return ApiResponse.success("칭호를 장착했습니다.", null);
    }

    @Operation(summary = "칭호 해제", description = "장착된 칭호를 해제합니다.")
    @PostMapping("/unequip")
    public ApiResponse<Void> unequipTitle(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        titleService.unequipTitle(userId);
        return ApiResponse.success("칭호를 해제했습니다.", null);
    }

    @Operation(summary = "최근 획득 칭호", description = "최근 획득한 칭호 목록을 조회합니다.")
    @GetMapping("/me/recent")
    public ApiResponse<List<UserTitleResponse>> getRecentTitles(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "5") int limit) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<UserTitleResponse> response = titleService.getRecentTitles(userId, limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "칭호 통계", description = "내 칭호 보유 통계를 조회합니다.")
    @GetMapping("/me/stats")
    public ApiResponse<Map<String, Long>> getTitleStats(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        long owned = titleService.getTitleCount(userId);
        long total = titleService.getAllTitles().size();
        
        return ApiResponse.success(Map.of(
                "owned", owned,
                "total", total
        ));
    }
}