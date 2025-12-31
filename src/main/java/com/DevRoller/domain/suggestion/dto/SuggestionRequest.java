package com.devroller.domain.suggestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 제안 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;

    private String description;

    private Long categoryId;

    @Size(max = 20, message = "난이도는 20자 이하여야 합니다.")
    private String difficulty;

    @Size(max = 500, message = "기술 스택은 500자 이하여야 합니다.")
    private String techStack;
}