package com.devroller.domain.category.controller;

import com.devroller.domain.category.dto.CategoryResponse;
import com.devroller.domain.category.service.CategoryService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "카테고리 API")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회", description = "활성화된 카테고리 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> categories = categoryService.getActiveCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @Operation(summary = "전체 카테고리 목록 (관리자)", description = "비활성화된 카테고리를 포함한 전체 목록을 조회합니다")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @Operation(summary = "카테고리 상세 조회")
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(
            @PathVariable Long categoryId) {
        CategoryResponse category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @Operation(summary = "카테고리 생성 (관리자)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String icon,
            @RequestParam(required = false) Integer displayOrder) {
        CategoryResponse category = categoryService.createCategory(name, description, icon, displayOrder);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @Operation(summary = "카테고리 수정 (관리자)")
    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String icon,
            @RequestParam(required = false) Integer displayOrder) {
        CategoryResponse category = categoryService.updateCategory(categoryId, name, description, icon, displayOrder);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @Operation(summary = "카테고리 비활성화 (관리자)")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateCategory(
            @PathVariable Long categoryId) {
        categoryService.deactivateCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "카테고리 활성화 (관리자)")
    @PostMapping("/{categoryId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateCategory(
            @PathVariable Long categoryId) {
        categoryService.activateCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
