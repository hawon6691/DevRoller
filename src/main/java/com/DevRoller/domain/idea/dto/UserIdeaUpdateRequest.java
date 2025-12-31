package com.devroller.domain.idea.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 사용자 프로젝트 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIdeaUpdateRequest {

    @Min(value = 0, message = "진행률은 0 이상이어야 합니다.")
    @Max(value = 100, message = "진행률은 100 이하여야 합니다.")
    private Integer progressPercent;

    @Size(max = 500, message = "메모는 500자 이하여야 합니다.")
    private String memo;

    @Size(max = 500, message = "GitHub URL은 500자 이하여야 합니다.")
    private String githubUrl;
}