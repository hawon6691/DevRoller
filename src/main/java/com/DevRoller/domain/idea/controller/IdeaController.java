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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 아이디어 API Controller
 */
@Tag(name = "Idea", description = "아이디어 API")
@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @Operation(summary = "아이디어 목록", description = "아이디어 목록을 페이징으로 조회합니다.")
    @GetMapping
    public ApiResponse<Page<IdeaResponse>> getIdeas(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<IdeaResponse> response = ideaService.getIdeas(pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "카테고리별 아이디어", description = "특정 카테고리의 아이디어 목록을 조회합니다.")
    @GetMapping("/category/{categoryId}")
    public ApiResponse<Page<IdeaResponse>> getIdeasByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<IdeaResponse> response = ideaService.getIdeasByCategory(categoryId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "아이디어 검색", description = "키워드로 아이디어를 검색합니다.")
    @GetMapping("/search")
    public ApiResponse<Page<IdeaResponse>> searchIdeas(
            @ModelAttribute IdeaSearchRequest request,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<IdeaResponse> response = ideaService.searchIdeas(request, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "인기 아이디어", description = "가장 많이 추첨된 아이디어 목록을 조회합니다.")
    @GetMapping("/popular")
    public ApiResponse<List<IdeaResponse>> getPopularIdeas(
            @RequestParam(defaultValue = "10") int limit) {
        List<IdeaResponse> response = ideaService.getPopularIdeas(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "평점 높은 아이디어", description = "평점이 높은 아이디어 목록을 조회합니다.")
    @GetMapping("/top-rated")
    public ApiResponse<List<IdeaResponse>> getTopRatedIdeas(
            @RequestParam(defaultValue = "10") int limit) {
        List<IdeaResponse> response = ideaService.getTopRatedIdeas(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "아이디어 상세 조회", description = "특정 아이디어의 상세 정보를 조회합니다.")
    @GetMapping("/{ideaId}")
    public ApiResponse<IdeaResponse> getIdea(@PathVariable Long ideaId) {
        IdeaResponse response = ideaService.getIdea(ideaId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "아이디어 생성 (관리자)", description = "새로운 아이디어를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<IdeaResponse> createIdea(@Valid @RequestBody IdeaCreateRequest request) {
        IdeaResponse response = ideaService.createIdea(request);
        return ApiResponse.success("아이디어가 생성되었습니다.", response);
    }

    @Operation(summary = "아이디어 수정 (관리자)", description = "아이디어 정보를 수정합니다.")
    @PutMapping("/{ideaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<IdeaResponse> updateIdea(
            @PathVariable Long ideaId,
            @Valid @RequestBody IdeaUpdateRequest request) {
        IdeaResponse response = ideaService.updateIdea(ideaId, request);
        return ApiResponse.success("아이디어가 수정되었습니다.", response);
    }

    @Operation(summary = "아이디어 삭제 (관리자)", description = "아이디어를 삭제(비활성화)합니다.")
    @DeleteMapping("/{ideaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteIdea(@PathVariable Long ideaId) {
        ideaService.deleteIdea(ideaId);
        return ApiResponse.success("아이디어가 삭제되었습니다.", null);
    }
}