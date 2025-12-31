package com.devroller.domain.category.controller;

import com.devroller.domain.category.dto.CategoryRequest;
import com.devroller.domain.category.dto.CategoryResponse;
import com.devroller.domain.category.service.CategoryService;
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
 * 카테고리 API Controller
 */
@Tag(name = "Category", description = "카테고리 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "활성 카테고리 목록", description = "활성화된 카테고리 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getActiveCategories() {
        List<CategoryResponse> response = categoryService.getActiveCategories();
        return ApiResponse.success(response);
    }

    @Operation(summary = "전체 카테고리 목록 (관리자)", description = "모든 카테고리 목록을 조회합니다.")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ApiResponse.success(response);
    }

    @Operation(summary = "카테고리 상세 조회", description = "특정 카테고리의 상세 정보를 조회합니다.")
    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        CategoryResponse response = categoryService.getCategory(categoryId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "카테고리 생성 (관리자)", description = "새로운 카테고리를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ApiResponse.success("카테고리가 생성되었습니다.", response);
    }

    @Operation(summary = "카테고리 수정 (관리자)", description = "카테고리 정보를 수정합니다.")
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ApiResponse.success("카테고리가 수정되었습니다.", response);
    }

    @Operation(summary = "카테고리 활성화 (관리자)", description = "카테고리를 활성화합니다.")
    @PatchMapping("/{categoryId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> activateCategory(@PathVariable Long categoryId) {
        categoryService.activateCategory(categoryId);
        return ApiResponse.success("카테고리가 활성화되었습니다.", null);
    }

    @Operation(summary = "카테고리 비활성화 (관리자)", description = "카테고리를 비활성화합니다.")
    @PatchMapping("/{categoryId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deactivateCategory(@PathVariable Long categoryId) {
        categoryService.deactivateCategory(categoryId);
        return ApiResponse.success("카테고리가 비활성화되었습니다.", null);
    }

    @Operation(summary = "카테고리 삭제 (관리자)", description = "카테고리를 삭제합니다.")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success("카테고리가 삭제되었습니다.", null);
    }
}