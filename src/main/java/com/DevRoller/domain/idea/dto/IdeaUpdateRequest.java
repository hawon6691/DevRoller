package com.devroller.domain.idea.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IdeaUpdateRequest {

    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다")
    private String title;

    private String description;

    private Long categoryId;

    private String difficulty;  // EASY, MEDIUM, HARD

    private Integer estimatedHours;

    @Size(max = 500)
    private String techStack;

    @Size(max = 500)
    private String referenceUrl;

    private List<Long> tagIds;
}
