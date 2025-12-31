package com.devroller.domain.pick.dto;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.pick.entity.PickHistory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 추첨 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickRequest {

    @NotNull(message = "추첨 방식은 필수입니다.")
    private PickHistory.PickMethod pickMethod;

    private Long categoryId;  // 카테고리 필터 (선택)

    private Idea.Difficulty difficulty;  // 난이도 필터 (선택)
}