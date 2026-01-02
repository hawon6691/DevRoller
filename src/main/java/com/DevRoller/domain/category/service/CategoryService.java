package com.devroller.domain.category.service;

import com.devroller.domain.category.dto.CategoryResponse;
import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.repository.CategoryRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 활성화된 카테고리 목록 조회
     */
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 전체 카테고리 목록 조회 (관리자)
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 단건 조회
     */
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 생성 (관리자)
     */
    @Transactional
    public CategoryResponse createCategory(String name, String description, String icon, Integer displayOrder) {
        Category category = Category.builder()
                .name(name)
                .description(description)
                .icon(icon)
                .displayOrder(displayOrder)
                .build();
        categoryRepository.save(category);
        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 수정 (관리자)
     */
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, String name, String description, 
                                           String icon, Integer displayOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        category.update(name, description, icon, displayOrder);
        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 비활성화 (관리자)
     */
    @Transactional
    public void deactivateCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        category.deactivate();
    }

    /**
     * 카테고리 활성화 (관리자)
     */
    @Transactional
    public void activateCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        category.activate();
    }
}
