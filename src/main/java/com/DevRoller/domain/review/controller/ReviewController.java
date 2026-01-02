package com.devroller.domain.review.controller;

import com.devroller.domain.review.dto.ReviewRequest;
import com.devroller.domain.review.dto.ReviewResponse;
import com.devroller.domain.review.service.ReviewService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "완료한 프로젝트에 대해 리뷰를 작성합니다")
    @PostMapping("/ideas/{ideaId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @Valid @RequestBody ReviewRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        ReviewResponse response = reviewService.createReview(userId, ideaId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "리뷰 수정", description = "내 리뷰를 수정합니다")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        ReviewResponse response = reviewService.updateReview(userId, reviewId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "리뷰 삭제", description = "내 리뷰를 삭제합니다")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId) {
        Long userId = Long.parseLong(jwt.getSubject());
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "리뷰 좋아요", description = "리뷰에 좋아요를 누릅니다")
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ApiResponse<Void>> likeReview(
            @PathVariable Long reviewId) {
        reviewService.likeReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰 좋아요를 취소합니다")
    @DeleteMapping("/{reviewId}/like")
    public ResponseEntity<ApiResponse<Void>> unlikeReview(
            @PathVariable Long reviewId) {
        reviewService.unlikeReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "아이디어별 리뷰 목록", description = "특정 아이디어의 리뷰 목록을 조회합니다")
    @GetMapping("/ideas/{ideaId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviewsByIdea(
            @PathVariable Long ideaId,
            Pageable pageable) {
        Page<ReviewResponse> reviews = reviewService.getReviewsByIdea(ideaId, pageable);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @Operation(summary = "내 리뷰 목록", description = "내가 작성한 리뷰 목록을 조회합니다")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getMyReviews(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<ReviewResponse> reviews = reviewService.getMyReviews(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 상세 정보를 조회합니다")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReview(
            @PathVariable Long reviewId) {
        ReviewResponse response = reviewService.getReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
