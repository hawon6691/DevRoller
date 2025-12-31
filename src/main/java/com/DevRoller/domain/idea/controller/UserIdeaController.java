package com.devroller.domain.idea.controller;

import com.devroller.domain.idea.dto.UserIdeaResponse;
import com.devroller.domain.idea.dto.UserIdeaUpdateRequest;
import com.devroller.domain.idea.service.UserIdeaService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 사용자 프로젝트 API Controller
 */
@Tag(name = "UserIdea", description = "사용자 프로젝트 API")
@RestController
@RequestMapping("/api/my-projects")
@RequiredArgsConstructor
public class UserIdeaController {

    private final UserIdeaService userIdeaService;

    @Operation(summary = "진행중인 프로젝트", description = "내가 진행중인 프로젝트 목록을 조회합니다.")
    @GetMapping("/in-progress")
    public ApiResponse<List<UserIdeaResponse>> getInProgressProjects(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<UserIdeaResponse> response = userIdeaService.getInProgressProjects(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "완료한 프로젝트", description = "내가 완료한 프로젝트 목록을 조회합니다.")
    @GetMapping("/completed")
    public ApiResponse<Page<UserIdeaResponse>> getCompletedProjects(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10, sort = "completedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<UserIdeaResponse> response = userIdeaService.getCompletedProjects(userId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로젝트 상세", description = "특정 프로젝트의 상세 정보를 조회합니다.")
    @GetMapping("/{ideaId}")
    public ApiResponse<UserIdeaResponse> getProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = userIdeaService.getProject(userId, ideaId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로젝트 시작", description = "새로운 프로젝트를 시작합니다.")
    @PostMapping("/{ideaId}/start")
    public ApiResponse<UserIdeaResponse> startProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = userIdeaService.startProject(userId, ideaId);
        return ApiResponse.success("프로젝트를 시작했습니다.", response);
    }

    @Operation(summary = "프로젝트 완료", description = "진행중인 프로젝트를 완료 처리합니다.")
    @PostMapping("/{ideaId}/complete")
    public ApiResponse<UserIdeaResponse> completeProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @RequestBody(required = false) Map<String, String> request) {
        Long userId = Long.parseLong(jwt.getSubject());
        String githubUrl = request != null ? request.get("githubUrl") : null;
        UserIdeaResponse response = userIdeaService.completeProject(userId, ideaId, githubUrl);
        return ApiResponse.success("프로젝트를 완료했습니다! 경험치를 획득했습니다.", response);
    }

    @Operation(summary = "프로젝트 포기", description = "진행중인 프로젝트를 포기합니다.")
    @PostMapping("/{ideaId}/abandon")
    public ApiResponse<UserIdeaResponse> abandonProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = userIdeaService.abandonProject(userId, ideaId);
        return ApiResponse.success("프로젝트를 포기했습니다.", response);
    }

    @Operation(summary = "진행률 업데이트", description = "프로젝트의 진행률을 업데이트합니다.")
    @PatchMapping("/{ideaId}")
    public ApiResponse<UserIdeaResponse> updateProgress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @Valid @RequestBody UserIdeaUpdateRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = userIdeaService.updateProgress(userId, ideaId, request);
        return ApiResponse.success("진행 상황이 업데이트되었습니다.", response);
    }

    @Operation(summary = "프로젝트 통계", description = "내 프로젝트 통계를 조회합니다.")
    @GetMapping("/stats")
    public ApiResponse<UserIdeaService.ProjectStats> getProjectStats(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaService.ProjectStats response = userIdeaService.getProjectStats(userId);
        return ApiResponse.success(response);
    }
}