package com.devroller.domain.suggestion.controller;

import com.devroller.domain.suggestion.dto.SuggestionRequest;
import com.devroller.domain.suggestion.dto.SuggestionResponse;
import com.devroller.domain.suggestion.service.SuggestionService;
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

import java.util.List;

@Tag(name = "Suggestion", description = "주제 제안 API")
@RestController
@RequestMapping("/api/v1/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Operation(summary = "제안 작성", description = "새로운 주제를 제안합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<SuggestionResponse>> createSuggestion(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SuggestionRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        SuggestionResponse response = suggestionService.createSuggestion(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제안 수정", description = "내 제안을 수정합니다")
    @PatchMapping("/{suggestionId}")
    public ResponseEntity<ApiResponse<SuggestionResponse>> updateSuggestion(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long suggestionId,
            @Valid @RequestBody SuggestionRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        SuggestionResponse response = suggestionService.updateSuggestion(userId, suggestionId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제안 삭제", description = "내 제안을 삭제합니다")
    @DeleteMapping("/{suggestionId}")
    public ResponseEntity<ApiResponse<Void>> deleteSuggestion(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long suggestionId) {
        Long userId = Long.parseLong(jwt.getSubject());
        suggestionService.deleteSuggestion(userId, suggestionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "제안 투표", description = "제안에 투표합니다")
    @PostMapping("/{suggestionId}/vote")
    public ResponseEntity<ApiResponse<Void>> vote(
            @PathVariable Long suggestionId) {
        suggestionService.vote(suggestionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "제안 투표 취소", description = "제안 투표를 취소합니다")
    @DeleteMapping("/{suggestionId}/vote")
    public ResponseEntity<ApiResponse<Void>> unvote(
            @PathVariable Long suggestionId) {
        suggestionService.unvote(suggestionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "제안 승인 (관리자)", description = "제안을 승인합니다")
    @PostMapping("/{suggestionId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SuggestionResponse>> approveSuggestion(
            @PathVariable Long suggestionId,
            @RequestParam(required = false) String adminComment) {
        SuggestionResponse response = suggestionService.approveSuggestion(suggestionId, adminComment);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제안 거절 (관리자)", description = "제안을 거절합니다")
    @PostMapping("/{suggestionId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SuggestionResponse>> rejectSuggestion(
            @PathVariable Long suggestionId,
            @RequestParam(required = false) String adminComment) {
        SuggestionResponse response = suggestionService.rejectSuggestion(suggestionId, adminComment);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제안 상세 조회", description = "제안 상세 정보를 조회합니다")
    @GetMapping("/{suggestionId}")
    public ResponseEntity<ApiResponse<SuggestionResponse>> getSuggestion(
            @PathVariable Long suggestionId) {
        SuggestionResponse response = suggestionService.getSuggestion(suggestionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "대기 중인 제안 목록", description = "대기 중인 제안 목록을 투표순으로 조회합니다")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<Page<SuggestionResponse>>> getPendingSuggestions(
            Pageable pageable) {
        Page<SuggestionResponse> suggestions = suggestionService.getPendingSuggestions(pageable);
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }

    @Operation(summary = "상태별 제안 목록", description = "상태별 제안 목록을 조회합니다")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<SuggestionResponse>>> getSuggestionsByStatus(
            @PathVariable String status,
            Pageable pageable) {
        Page<SuggestionResponse> suggestions = suggestionService.getSuggestionsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }

    @Operation(summary = "내 제안 목록", description = "내가 작성한 제안 목록을 조회합니다")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<SuggestionResponse>>> getMySuggestions(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<SuggestionResponse> suggestions = suggestionService.getMySuggestions(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }

    @Operation(summary = "인기 제안 TOP 10", description = "투표수가 높은 TOP 10 제안을 조회합니다")
    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<SuggestionResponse>>> getTopSuggestions() {
        List<SuggestionResponse> suggestions = suggestionService.getTopSuggestions();
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }
}
