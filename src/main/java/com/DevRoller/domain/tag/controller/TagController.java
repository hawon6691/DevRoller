package com.devroller.domain.tag.controller;

import com.devroller.domain.tag.dto.TagRequest;
import com.devroller.domain.tag.dto.TagResponse;
import com.devroller.domain.tag.service.TagService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 태그 API Controller
 */
@Tag(name = "Tag", description = "태그 API")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "전체 태그 목록", description = "모든 태그 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<TagResponse>> getAllTags() {
        List<TagResponse> response = tagService.getAllTags();
        return ApiResponse.success(response);
    }

    @Operation(summary = "태그 검색", description = "키워드로 태그를 검색합니다.")
    @GetMapping("/search")
    public ApiResponse<List<TagResponse>> searchTags(@RequestParam String keyword) {
        List<TagResponse> response = tagService.searchTags(keyword);
        return ApiResponse.success(response);
    }

    @Operation(summary = "인기 태그", description = "가장 많이 사용된 태그 목록을 조회합니다.")
    @GetMapping("/popular")
    public ApiResponse<List<TagResponse>> getPopularTags(
            @RequestParam(defaultValue = "10") int limit) {
        List<TagResponse> response = tagService.getPopularTags(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "태그 생성 (관리자)", description = "새로운 태그를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        TagResponse response = tagService.createTag(request);
        return ApiResponse.success("태그가 생성되었습니다.", response);
    }

    @Operation(summary = "태그 수정 (관리자)", description = "태그 정보를 수정합니다.")
    @PutMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TagResponse> updateTag(
            @PathVariable Long tagId,
            @Valid @RequestBody TagRequest request) {
        TagResponse response = tagService.updateTag(tagId, request);
        return ApiResponse.success("태그가 수정되었습니다.", response);
    }

    @Operation(summary = "태그 삭제 (관리자)", description = "태그를 삭제합니다.")
    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ApiResponse.success("태그가 삭제되었습니다.", null);
    }
}