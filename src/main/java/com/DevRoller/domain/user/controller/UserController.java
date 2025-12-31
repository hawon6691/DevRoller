package com.devroller.domain.user.controller;

import com.devroller.domain.user.dto.UserProfileResponse;
import com.devroller.domain.user.dto.UserUpdateRequest;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 사용자 API Controller
 */
@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 프로필 조회", description = "로그인한 사용자의 프로필을 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserProfileResponse response = userService.getProfile(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "사용자 프로필 조회", description = "특정 사용자의 프로필을 조회합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getProfile(@PathVariable Long userId) {
        UserProfileResponse response = userService.getProfile(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로필 수정", description = "내 프로필 정보를 수정합니다.")
    @PatchMapping("/me")
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserUpdateRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserProfileResponse response = userService.updateProfile(userId, request);
        return ApiResponse.success("프로필이 수정되었습니다.", response);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PatchMapping("/me/password")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(jwt.getSubject());
        userService.changePassword(userId, request.get("currentPassword"), request.get("newPassword"));
        return ApiResponse.success("비밀번호가 변경되었습니다.", null);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @DeleteMapping("/me")
    public ApiResponse<Void> deactivate(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(jwt.getSubject());
        userService.deactivate(userId, request.get("password"));
        return ApiResponse.success("회원 탈퇴가 완료되었습니다.", null);
    }

    @Operation(summary = "레벨 랭킹", description = "레벨 기준 상위 사용자 목록을 조회합니다.")
    @GetMapping("/ranking/level")
    public ApiResponse<List<UserProfileResponse>> getLevelRanking(
            @RequestParam(defaultValue = "10") int limit) {
        List<UserProfileResponse> response = userService.getLevelRanking(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "완료 프로젝트 랭킹", description = "완료 프로젝트 기준 상위 사용자 목록을 조회합니다.")
    @GetMapping("/ranking/completed")
    public ApiResponse<List<UserProfileResponse>> getCompletedRanking(
            @RequestParam(defaultValue = "10") int limit) {
        List<UserProfileResponse> response = userService.getCompletedRanking(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "스트릭 랭킹", description = "연속 기록 기준 상위 사용자 목록을 조회합니다.")
    @GetMapping("/ranking/streak")
    public ApiResponse<List<UserProfileResponse>> getStreakRanking(
            @RequestParam(defaultValue = "10") int limit) {
        List<UserProfileResponse> response = userService.getStreakRanking(limit);
        return ApiResponse.success(response);
    }
}