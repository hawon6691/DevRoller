package com.devroller.domain.review.dto;

import com.devroller.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private Long ideaId;
    private String ideaTitle;
    private Long userId;
    private String userNickname;
    private Integer rating;
    private String content;
    private Integer actualHours;
    private String difficultyFeedback;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .ideaId(review.getIdea().getId())
                .ideaTitle(review.getIdea().getTitle())
                .userId(review.getUser().getId())
                .userNickname(review.getUser().getNickname())
                .rating(review.getRating())
                .content(review.getContent())
                .actualHours(review.getActualHours())
                .difficultyFeedback(review.getDifficultyFeedback() != null ? 
                        review.getDifficultyFeedback().name() : null)
                .likeCount(review.getLikeCount())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
