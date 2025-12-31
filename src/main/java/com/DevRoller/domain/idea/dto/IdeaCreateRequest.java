package com.devroller.domain.idea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IdeaCreateRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다")
    private String title;

    private String description;

    @NotNull(message = "카테고리는 필수입니다")
    private Long categoryId;

    @NotBlank(message = "난이도는 필수입니다")
    private String difficulty;  // EASY, MEDIUM, HARD

    private Integer estimatedHours;

    @Size(max = 500)
    private String techStack;

    @Size(max = 500)
    private String referenceUrl;

    private List<Long> tagIds;
}
