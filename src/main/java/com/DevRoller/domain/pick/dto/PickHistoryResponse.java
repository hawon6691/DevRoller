package com.devroller.domain.pick.dto;

import com.devroller.domain.pick.entity.PickHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 추첨 기록 응답 DTO
 */
@Getter
@Builder
public class PickHistoryResponse {

    private Long pickHistoryId;
    private Long ideaId;
    private String ideaTitle;
    private String categoryName;
    private String pickMethod;
    private String categoryFilter;
    private String difficultyFilter;
    private LocalDateTime createdAt;

    public static PickHistoryResponse from(PickHistory history) {
        return PickHistoryResponse.builder()
                .pickHistoryId(history.getId())
                .ideaId(history.getIdea().getId())
                .ideaTitle(history.getIdea().getTitle())
                .categoryName(history.getIdea().getCategory().getName())
                .pickMethod(history.getPickMethod().name())
                .categoryFilter(history.getCategoryFilter())
                .difficultyFilter(history.getDifficultyFilter())
                .createdAt(history.getCreatedAt())
                .build();
    }
}