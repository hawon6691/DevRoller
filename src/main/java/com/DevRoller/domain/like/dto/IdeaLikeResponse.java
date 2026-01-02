package com.devroller.domain.like.dto;

import com.devroller.domain.idea.dto.IdeaResponse;
import com.devroller.domain.like.entity.IdeaLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdeaLikeResponse {

    private Long id;
    private Long userId;
    private IdeaResponse idea;
    private LocalDateTime likedAt;

    public static IdeaLikeResponse from(IdeaLike ideaLike) {
        return IdeaLikeResponse.builder()
                .id(ideaLike.getId())
                .userId(ideaLike.getUser().getId())
                .idea(IdeaResponse.from(ideaLike.getIdea()))
                .likedAt(ideaLike.getCreatedAt())
                .build();
    }
}
