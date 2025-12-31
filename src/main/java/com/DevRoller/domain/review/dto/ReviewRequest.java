package com.devroller.domain.review.dto;

import com.devroller.domain.review.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 리뷰 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    @NotNull(message = "아이디어 ID는 필수입니다.")
    private Long ideaId;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private Integer rating;

    private String content;

    @Min(value = 0, message = "실제 소요 시간은 0 이상이어야 합니다.")
    private Integer actualHours;

    private Review.DifficultyFeedback difficultyFeedback;
}