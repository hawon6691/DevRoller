package com.devroller.domain.idea.dto;

import com.devroller.domain.idea.entity.UserIdea;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserIdeaResponse {

    private Long id;
    private Long ideaId;
    private String ideaTitle;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String githubUrl;
    private Integer progressPercent;
    private String memo;

    public static UserIdeaResponse from(UserIdea userIdea) {
        return UserIdeaResponse.builder()
                .id(userIdea.getId())
                .ideaId(userIdea.getIdea().getId())
                .ideaTitle(userIdea.getIdea().getTitle())
                .status(userIdea.getStatus().name())
                .startedAt(userIdea.getStartedAt())
                .completedAt(userIdea.getCompletedAt())
                .githubUrl(userIdea.getGithubUrl())
                .progressPercent(userIdea.getProgressPercent())
                .memo(userIdea.getMemo())
                .build();
    }
}
