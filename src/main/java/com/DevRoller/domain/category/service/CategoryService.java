package com.devroller.domain.category.service;

import com.devroller.domain.category.dto.CategoryRequest;
import com.devroller.domain.category.dto.CategoryResponse;
import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.repository.CategoryRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 조회 (ID)
     */
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    /**
     * 활성 카테고리 목록 조회
     */
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 전체 카테고리 목록 조회 (관리자용)
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 상세 조회
     */
    public CategoryResponse getCategory(Long categoryId) {
        Category category = findById(categoryId);
        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 생성
     */
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // 이름 중복 체크
        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 카테고리명입니다.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder())
                .build();

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created: {}", savedCategory.getName());

        return CategoryResponse.from(savedCategory);
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        Category category = findById(categoryId);

        // 이름 변경 시 중복 체크
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 카테고리명입니다.");
            }
        }

        category.update(
                request.getName(),
                request.getDescription(),
                request.getIcon(),
                request.getDisplayOrder()
        );

        log.info("Category updated: {}", categoryId);
        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 활성화
     */
    @Transactional
    public void activateCategory(Long categoryId) {
        Category category = findById(categoryId);
        category.activate();
        log.info("Category activated: {}", categoryId);
    }

    /**
     * 카테고리 비활성화
     */
    @Transactional
    public void deactivateCategory(Long categoryId) {
        Category category = findById(categoryId);
        category.deactivate();
        log.info("Category deactivated: {}", categoryId);
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = findById(categoryId);
        categoryRepository.delete(category);
        log.info("Category deleted: {}", categoryId);
    }
}