package com.devroller.domain.idea.dto;

import com.devroller.domain.idea.entity.UserIdea;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 프로젝트 응답 DTO
 */
@Getter
@Builder
public class UserIdeaResponse {

    private Long userIdeaId;
    private Long ideaId;
    private String ideaTitle;
    private String categoryName;
    private String difficulty;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String githubUrl;
    private Integer progressPercent;
    private String memo;
    private Integer experiencePoints;

    public static UserIdeaResponse from(UserIdea userIdea) {
        return UserIdeaResponse.builder()
                .userIdeaId(userIdea.getId())
                .ideaId(userIdea.getIdea().getId())
                .ideaTitle(userIdea.getIdea().getTitle())
                .categoryName(userIdea.getIdea().getCategory().getName())
                .difficulty(userIdea.getIdea().getDifficulty().name())
                .status(userIdea.getStatus().name())
                .startedAt(userIdea.getStartedAt())
                .completedAt(userIdea.getCompletedAt())
                .githubUrl(userIdea.getGithubUrl())
                .progressPercent(userIdea.getProgressPercent())
                .memo(userIdea.getMemo())
                .experiencePoints(userIdea.getIdea().getExperiencePoints())
                .build();
    }
}