package com.devroller.domain.suggestion.dto;

import com.devroller.domain.suggestion.entity.Suggestion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 제안 응답 DTO
 */
@Getter
@Builder
public class SuggestionResponse {

    private Long suggestionId;
    private Long userId;
    private String userNickname;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private String techStack;
    private String status;
    private String adminComment;
    private Integer voteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SuggestionResponse from(Suggestion suggestion) {
        return SuggestionResponse.builder()
                .suggestionId(suggestion.getId())
                .userId(suggestion.getUser().getId())
                .userNickname(suggestion.getUser().getNickname())
                .title(suggestion.getTitle())
                .description(suggestion.getDescription())
                .categoryId(suggestion.getCategory() != null ? suggestion.getCategory().getId() : null)
                .categoryName(suggestion.getCategory() != null ? suggestion.getCategory().getName() : null)
                .difficulty(suggestion.getDifficulty())
                .techStack(suggestion.getTechStack())
                .status(suggestion.getStatus().name())
                .adminComment(suggestion.getAdminComment())
                .voteCount(suggestion.getVoteCount())
                .createdAt(suggestion.getCreatedAt())
                .updatedAt(suggestion.getUpdatedAt())
                .build();
    }
}