package com.devroller.domain.idea.dto;

import com.devroller.domain.idea.entity.Idea;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class IdeaResponse {

    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private Long categoryId;
    private String difficulty;
    private Integer estimatedHours;
    private String techStack;
    private String referenceUrl;
    private Integer pickCount;
    private Integer completedCount;
    private Integer likeCount;
    private Double averageRating;
    private List<String> tags;
    private LocalDateTime createdAt;

    public static IdeaResponse from(Idea idea) {
        return IdeaResponse.builder()
                .id(idea.getId())
                .title(idea.getTitle())
                .description(idea.getDescription())
                .categoryName(idea.getCategory().getName())
                .categoryId(idea.getCategory().getId())
                .difficulty(idea.getDifficulty().name())
                .estimatedHours(idea.getEstimatedHours())
                .techStack(idea.getTechStack())
                .referenceUrl(idea.getReferenceUrl())
                .pickCount(idea.getPickCount())
                .completedCount(idea.getCompletedCount())
                .likeCount(idea.getLikeCount())
                .averageRating(idea.getAverageRating())
                .tags(idea.getIdeaTags().stream()
                        .map(it -> it.getTag().getName())
                        .collect(Collectors.toList()))
                .createdAt(idea.getCreatedAt())
                .build();
    }
}
