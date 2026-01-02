package com.devroller.domain.tag.controller;

import com.devroller.domain.tag.dto.TagResponse;
import com.devroller.domain.tag.service.TagService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag", description = "태그 API")
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 목록 조회", description = "전체 태그 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @Operation(summary = "태그 검색", description = "키워드로 태그를 검색합니다")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TagResponse>>> searchTags(
            @RequestParam String keyword) {
        List<TagResponse> tags = tagService.searchTags(keyword);
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @Operation(summary = "태그 상세 조회")
    @GetMapping("/{tagId}")
    public ResponseEntity<ApiResponse<TagResponse>> getTag(
            @PathVariable Long tagId) {
        TagResponse tag = tagService.getTag(tagId);
        return ResponseEntity.ok(ApiResponse.success(tag));
    }

    @Operation(summary = "태그 생성 (관리자)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TagResponse>> createTag(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "#6B7280") String color) {
        TagResponse tag = tagService.createTag(name, color);
        return ResponseEntity.ok(ApiResponse.success(tag));
    }

    @Operation(summary = "태그 수정 (관리자)")
    @PatchMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(
            @PathVariable Long tagId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {
        TagResponse tag = tagService.updateTag(tagId, name, color);
        return ResponseEntity.ok(ApiResponse.success(tag));
    }

    @Operation(summary = "태그 삭제 (관리자)")
    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTag(
            @PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
