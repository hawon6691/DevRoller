package com.devroller.domain.idea.dto;

import com.devroller.domain.idea.entity.Idea;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 아이디어 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdeaUpdateRequest {

    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;

    private String description;

    private Long categoryId;

    private Idea.Difficulty difficulty;

    private Integer estimatedHours;

    @Size(max = 500, message = "기술 스택은 500자 이하여야 합니다.")
    private String techStack;

    @Size(max = 500, message = "참고 URL은 500자 이하여야 합니다.")
    private String referenceUrl;

    private List<Long> tagIds;
}