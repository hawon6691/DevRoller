package com.devroller.domain.idea.controller;

import com.devroller.domain.idea.dto.*;
import com.devroller.domain.idea.service.IdeaService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Idea", description = "아이디어/주제 API")
@RestController
@RequestMapping({"/api/v1/ideas", "/api/ideas"})  // v1 경로와 v1 없는 경로 모두 지원
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @Operation(summary = "아이디어 목록 조회", description = "전체 아이디어를 페이징 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<IdeaResponse>>> getAllIdeas(Pageable pageable) {
        Page<IdeaResponse> ideas = ideaService.getAllIdeas(pageable);
        return ResponseEntity.ok(ApiResponse.success(ideas));
    }

    @Operation(summary = "아이디어 단건 조회")
    @GetMapping("/{ideaId}")
    public ResponseEntity<ApiResponse<IdeaResponse>> getIdea(@PathVariable Long ideaId) {
        IdeaResponse idea = ideaService.getIdeaById(ideaId);
        return ResponseEntity.ok(ApiResponse.success(idea));
    }

    @Operation(summary = "카테고리별 아이디어 조회")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<IdeaResponse>>> getIdeasByCategory(
            @PathVariable Long categoryId, Pageable pageable) {
        Page<IdeaResponse> ideas = ideaService.getIdeasByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(ideas));
    }

    @Operation(summary = "난이도별 아이디어 조회")
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<ApiResponse<Page<IdeaResponse>>> getIdeasByDifficulty(
            @PathVariable String difficulty, Pageable pageable) {
        Page<IdeaResponse> ideas = ideaService.getIdeasByDifficulty(difficulty, pageable);
        return ResponseEntity.ok(ApiResponse.success(ideas));
    }

    @Operation(summary = "인기 아이디어 조회")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<IdeaResponse>>> getPopularIdeas(Pageable pageable) {
        Page<IdeaResponse> ideas = ideaService.getPopularIdeas(pageable);
        return ResponseEntity.ok(ApiResponse.success(ideas));
    }

    @Operation(summary = "평점 높은 아이디어 조회")
    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<Page<IdeaResponse>>> getTopRatedIdeas(Pageable pageable) {
        Page<IdeaResponse> ideas = ideaService.getTopRatedIdeas(pageable);
        return ResponseEntity.ok(ApiResponse.success(ideas));
    }

    @Operation(summary = "아이디어 검색")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<IdeaResponse>>> searchIdeas(
            @RequestParam String keyword, Pageable pageable) {
        Page<IdeaResponse> ideas = ideaService.searchIdeas(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(ideas));
    }

    @Operation(summary = "아이디어 생성 (관리자)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<IdeaResponse>> createIdea(
            @Valid @RequestBody IdeaCreateRequest request) {
        IdeaResponse idea = ideaService.createIdea(request);
        return ResponseEntity.ok(ApiResponse.success(idea));
    }

    @Operation(summary = "아이디어 수정 (관리자)")
    @PatchMapping("/{ideaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<IdeaResponse>> updateIdea(
            @PathVariable Long ideaId,
            @Valid @RequestBody IdeaUpdateRequest request) {
        IdeaResponse idea = ideaService.updateIdea(ideaId, request);
        return ResponseEntity.ok(ApiResponse.success(idea));
    }

    @Operation(summary = "아이디어 삭제 (관리자)")
    @DeleteMapping("/{ideaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteIdea(@PathVariable Long ideaId) {
        ideaService.deleteIdea(ideaId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ==================== 프로젝트 진행 API ====================

    @Operation(summary = "프로젝트 시작", description = "아이디어를 선택하여 프로젝트를 시작합니다")
    @PostMapping("/{ideaId}/start")
    public ResponseEntity<ApiResponse<UserIdeaResponse>> startProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = ideaService.startProject(userId, ideaId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "프로젝트 완료")
    @PostMapping("/{ideaId}/complete")
    public ResponseEntity<ApiResponse<UserIdeaResponse>> completeProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @RequestParam(required = false) String githubUrl) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = ideaService.completeProject(userId, ideaId, githubUrl);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "프로젝트 포기")
    @PostMapping("/{ideaId}/abandon")
    public ResponseEntity<ApiResponse<UserIdeaResponse>> abandonProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = ideaService.abandonProject(userId, ideaId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "진행률 업데이트")
    @PatchMapping("/{ideaId}/progress")
    public ResponseEntity<ApiResponse<UserIdeaResponse>> updateProgress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @RequestParam Integer progress) {
        Long userId = Long.parseLong(jwt.getSubject());
        UserIdeaResponse response = ideaService.updateProgress(userId, ideaId, progress);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 프로젝트 목록")
    @GetMapping("/my-projects")
    public ResponseEntity<ApiResponse<Page<UserIdeaResponse>>> getMyProjects(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<UserIdeaResponse> projects = ideaService.getMyProjects(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @Operation(summary = "상태별 내 프로젝트 목록")
    @GetMapping("/my-projects/{status}")
    public ResponseEntity<ApiResponse<Page<UserIdeaResponse>>> getMyProjectsByStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String status,
            Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<UserIdeaResponse> projects = ideaService.getMyProjectsByStatus(userId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
}
