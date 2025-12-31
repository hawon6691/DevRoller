package com.devroller.domain.review.dto;

import com.devroller.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 리뷰 응답 DTO
 */
@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;
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

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .ideaId(review.getIdea().getId())
                .ideaTitle(review.getIdea().getTitle())
                .userId(review.getUser().getId())
                .userNickname(review.getUser().getNickname())
                .rating(review.getRating())
                .content(review.getContent())
                .actualHours(review.getActualHours())
                .difficultyFeedback(review.getDifficultyFeedback() != null 
                        ? review.getDifficultyFeedback().name() : null)
                .likeCount(review.getLikeCount())
                .createdAt(review.getCreatedAt())
                .build();
    }
}