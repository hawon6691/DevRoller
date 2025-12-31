package com.devroller.domain.user.controller;

import com.devroller.domain.user.dto.UserResponse;
import com.devroller.domain.user.dto.UserUpdateRequest;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "프로필 수정", description = "프로필 정보를 수정합니다")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserUpdateRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다")
    @PostMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        Long userId = Long.parseLong(jwt.getSubject());
        userService.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "칭호 장착", description = "칭호를 장착합니다")
    @PostMapping("/me/title/{titleId}")
    public ResponseEntity<ApiResponse<UserResponse>> equipTitle(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long titleId) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserResponse response = userService.equipTitle(userId, titleId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "사용자 조회", description = "특정 사용자의 공개 정보를 조회합니다")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "레벨 랭킹", description = "레벨 기준 랭킹을 조회합니다")
    @GetMapping("/ranking/level")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getLevelRanking(Pageable pageable) {
        Page<UserResponse> ranking = userService.getLevelRanking(pageable);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    @Operation(summary = "완료 랭킹", description = "완료 프로젝트 기준 랭킹을 조회합니다")
    @GetMapping("/ranking/completed")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getCompletedRanking(Pageable pageable) {
        Page<UserResponse> ranking = userService.getCompletedRanking(pageable);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    @Operation(summary = "스트릭 랭킹", description = "스트릭 기준 랭킹을 조회합니다")
    @GetMapping("/ranking/streak")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getStreakRanking(Pageable pageable) {
        Page<UserResponse> ranking = userService.getStreakRanking(pageable);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴합니다")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        userService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
