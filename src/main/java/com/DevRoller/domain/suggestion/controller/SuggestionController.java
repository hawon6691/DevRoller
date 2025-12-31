package com.devroller.domain.suggestion.controller;

import com.devroller.domain.suggestion.dto.SuggestionRequest;
import com.devroller.domain.suggestion.dto.SuggestionResponse;
import com.devroller.domain.suggestion.entity.Suggestion;
import com.devroller.domain.suggestion.service.SuggestionService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 주제 제안 API Controller
 */
@Tag(name = "Suggestion", description = "주제 제안 API")
@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Operation(summary = "대기중인 제안", description = "대기중인 제안 목록을 투표순으로 조회합니다.")
    @GetMapping("/pending")
    public ApiResponse<Page<SuggestionResponse>> getPendingSuggestions(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<SuggestionResponse> response = suggestionService.getPendingSuggestions(pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "상태별 제안", description = "특정 상태의 제안 목록을 조회합니다.")
    @GetMapping("/status/{status}")
    public ApiResponse<Page<SuggestionResponse>> getSuggestionsByStatus(
            @PathVariable Suggestion.Status status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SuggestionResponse> response = suggestionService.getSuggestionsByStatus(status, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "내 제안 목록", description = "내가 작성한 제안 목록을 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<Page<SuggestionResponse>> getMySuggestions(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<SuggestionResponse> response = suggestionService.getUserSuggestions(userId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "제안 상세", description = "제안 상세 정보를 조회합니다.")
    @GetMapping("/{suggestionId}")
    public ApiResponse<SuggestionResponse> getSuggestion(@PathVariable Long suggestionId) {
        SuggestionResponse response = suggestionService.getSuggestion(suggestionId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "제안 작성", description = "새로운 주제를 제안합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SuggestionResponse> createSuggestion(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SuggestionRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        SuggestionResponse response = suggestionService.createSuggestion(userId, request);
        return ApiResponse.success("제안이 등록되었습니다.", response);
    }

    @Operation(summary = "제안 수정", description = "내 제안을 수정합니다.")
    @PutMapping("/{suggestionId}")
    public ApiResponse<SuggestionResponse> updateSuggestion(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long suggestionId,
            @Valid @RequestBody SuggestionRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        SuggestionResponse response = suggestionService.updateSuggestion(userId, suggestionId, request);
        return ApiResponse.success("제안이 수정되었습니다.", response);
    }

    @Operation(summary = "제안 삭제", description = "내 제안을 삭제합니다.")
    @DeleteMapping("/{suggestionId}")
    public ApiResponse<Void> deleteSuggestion(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long suggestionId) {
        Long userId = Long.parseLong(jwt.getSubject());
        suggestionService.deleteSuggestion(userId, suggestionId);
        return ApiResponse.success("제안이 삭제되었습니다.", null);
    }

    @Operation(summary = "제안 투표", description = "제안에 투표합니다.")
    @PostMapping("/{suggestionId}/vote")
    public ApiResponse<Void> voteSuggestion(@PathVariable Long suggestionId) {
        suggestionService.voteSuggestion(suggestionId);
        return ApiResponse.success("투표했습니다.", null);
    }

    @Operation(summary = "제안 승인 (관리자)", description = "제안을 승인합니다.")
    @PostMapping("/{suggestionId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SuggestionResponse> approveSuggestion(
            @PathVariable Long suggestionId,
            @RequestBody(required = false) Map<String, String> request) {
        String adminComment = request != null ? request.get("adminComment") : null;
        SuggestionResponse response = suggestionService.approveSuggestion(suggestionId, adminComment);
        return ApiResponse.success("제안이 승인되었습니다.", response);
    }

    @Operation(summary = "제안 거절 (관리자)", description = "제안을 거절합니다.")
    @PostMapping("/{suggestionId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SuggestionResponse> rejectSuggestion(
            @PathVariable Long suggestionId,
            @RequestBody(required = false) Map<String, String> request) {
        String adminComment = request != null ? request.get("adminComment") : null;
        SuggestionResponse response = suggestionService.rejectSuggestion(suggestionId, adminComment);
        return ApiResponse.success("제안이 거절되었습니다.", response);
    }

    @Operation(summary = "인기 제안", description = "투표수가 높은 제안 목록을 조회합니다.")
    @GetMapping("/top-voted")
    public ApiResponse<List<SuggestionResponse>> getTopVotedSuggestions(
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionResponse> response = suggestionService.getTopVotedSuggestions(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "최근 승인 제안", description = "최근 승인된 제안 목록을 조회합니다.")
    @GetMapping("/recent-approved")
    public ApiResponse<List<SuggestionResponse>> getRecentApproved(
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionResponse> response = suggestionService.getRecentApproved(limit);
        return ApiResponse.success(response);
    }
}