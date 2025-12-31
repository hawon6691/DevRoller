package com.devroller.domain.gamification.controller;

import com.devroller.domain.gamification.dto.AchievementResponse;
import com.devroller.domain.gamification.dto.StatusWindowResponse;
import com.devroller.domain.gamification.dto.TitleResponse;
import com.devroller.domain.gamification.service.GamificationService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gamification", description = "게이미피케이션 API")
@RestController
@RequestMapping("/api/v1/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @Operation(summary = "스테이터스 창 조회", description = "웹소설/웹툰 스타일의 스테이터스 창을 조회합니다")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<StatusWindowResponse>> getStatusWindow(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        StatusWindowResponse response = gamificationService.getStatusWindow(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "전체 업적 목록", description = "전체 업적 목록과 진행도를 조회합니다")
    @GetMapping("/achievements")
    public ResponseEntity<ApiResponse<List<AchievementResponse>>> getAllAchievements(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<AchievementResponse> achievements = gamificationService.getAllAchievements(userId);
        return ResponseEntity.ok(ApiResponse.success(achievements));
    }

    @Operation(summary = "달성한 업적 목록", description = "달성한 업적만 조회합니다")
    @GetMapping("/achievements/completed")
    public ResponseEntity<ApiResponse<List<AchievementResponse>>> getCompletedAchievements(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<AchievementResponse> achievements = gamificationService.getCompletedAchievements(userId);
        return ResponseEntity.ok(ApiResponse.success(achievements));
    }

    @Operation(summary = "전체 칭호 목록", description = "전체 칭호 목록과 보유 여부를 조회합니다")
    @GetMapping("/titles")
    public ResponseEntity<ApiResponse<List<TitleResponse>>> getAllTitles(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<TitleResponse> titles = gamificationService.getAllTitles(userId);
        return ResponseEntity.ok(ApiResponse.success(titles));
    }

    @Operation(summary = "보유한 칭호 목록", description = "보유한 칭호만 조회합니다")
    @GetMapping("/titles/owned")
    public ResponseEntity<ApiResponse<List<TitleResponse>>> getOwnedTitles(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<TitleResponse> titles = gamificationService.getOwnedTitles(userId);
        return ResponseEntity.ok(ApiResponse.success(titles));
    }

    @Operation(summary = "칭호 장착", description = "보유한 칭호를 장착합니다")
    @PostMapping("/titles/{titleId}/equip")
    public ResponseEntity<ApiResponse<Void>> equipTitle(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long titleId) {
        Long userId = Long.parseLong(jwt.getSubject());
        gamificationService.equipTitle(userId, titleId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "칭호 해제", description = "장착된 칭호를 해제합니다")
    @PostMapping("/titles/unequip")
    public ResponseEntity<ApiResponse<Void>> unequipTitle(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        gamificationService.unequipTitle(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
