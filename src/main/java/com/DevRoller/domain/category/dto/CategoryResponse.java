package com.devroller.domain.category.dto;

import com.devroller.domain.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

/**
 * 카테고리 응답 DTO
 */
@Getter
@Builder
public class CategoryResponse {

    private Long categoryId;
    private String name;
    private String description;
    private String icon;
    private Integer displayOrder;
    private Boolean isActive;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .displayOrder(category.getDisplayOrder())
                .isActive(category.getIsActive())
                .build();
    }
}