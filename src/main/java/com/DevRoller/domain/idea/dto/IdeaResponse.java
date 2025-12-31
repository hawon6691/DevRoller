package com.devroller.domain.idea.dto;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 아이디어 응답 DTO
 */
@Getter
@Builder
public class IdeaResponse {

    private Long ideaId;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private Integer experiencePoints;
    private Integer estimatedHours;
    private String techStack;
    private String referenceUrl;
    private Integer pickCount;
    private Integer completedCount;
    private Integer likeCount;
    private Double averageRating;
    private List<TagInfo> tags;
    private LocalDateTime createdAt;

    public static IdeaResponse from(Idea idea, List<Tag> tags) {
        return IdeaResponse.builder()
                .ideaId(idea.getId())
                .title(idea.getTitle())
                .description(idea.getDescription())
                .categoryId(idea.getCategory().getId())
                .categoryName(idea.getCategory().getName())
                .difficulty(idea.getDifficulty().name())
                .experiencePoints(idea.getExperiencePoints())
                .estimatedHours(idea.getEstimatedHours())
                .techStack(idea.getTechStack())
                .referenceUrl(idea.getReferenceUrl())
                .pickCount(idea.getPickCount())
                .completedCount(idea.getCompletedCount())
                .likeCount(idea.getLikeCount())
                .averageRating(idea.getAverageRating())
                .tags(tags.stream().map(TagInfo::from).collect(Collectors.toList()))
                .createdAt(idea.getCreatedAt())
                .build();
    }

    @Getter
    @Builder
    public static class TagInfo {
        private Long tagId;
        private String name;
        private String color;

        public static TagInfo from(Tag tag) {
            return TagInfo.builder()
                    .tagId(tag.getId())
                    .name(tag.getName())
                    .color(tag.getColor())
                    .build();
        }
    }
}