package com.devroller.domain.pick.dto;

import com.devroller.domain.idea.entity.Idea;
import lombok.Builder;
import lombok.Getter;

/**
 * 추첨 결과 응답 DTO
 */
@Getter
@Builder
public class PickResponse {

    private Long pickHistoryId;
    private Long ideaId;
    private String title;
    private String description;
    private String categoryName;
    private String difficulty;
    private Integer experiencePoints;
    private Integer estimatedHours;
    private String techStack;
    private String referenceUrl;
    private Integer pickCount;
    private Double averageRating;

    public static PickResponse from(Idea idea, Long pickHistoryId) {
        return PickResponse.builder()
                .pickHistoryId(pickHistoryId)
                .ideaId(idea.getId())
                .title(idea.getTitle())
                .description(idea.getDescription())
                .categoryName(idea.getCategory().getName())
                .difficulty(idea.getDifficulty().name())
                .experiencePoints(idea.getExperiencePoints())
                .estimatedHours(idea.getEstimatedHours())
                .techStack(idea.getTechStack())
                .referenceUrl(idea.getReferenceUrl())
                .pickCount(idea.getPickCount())
                .averageRating(idea.getAverageRating())
                .build();
    }
}