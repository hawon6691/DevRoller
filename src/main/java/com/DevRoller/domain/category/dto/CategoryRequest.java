package com.devroller.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 카테고리 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "카테고리명은 필수입니다.")
    @Size(max = 50, message = "카테고리명은 50자 이하여야 합니다.")
    private String name;

    @Size(max = 200, message = "설명은 200자 이하여야 합니다.")
    private String description;

    @Size(max = 50, message = "아이콘은 50자 이하여야 합니다.")
    private String icon;

    private Integer displayOrder;
}