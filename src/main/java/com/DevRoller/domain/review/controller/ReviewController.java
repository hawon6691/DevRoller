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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰 API Controller
 */
@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "아이디어 리뷰 목록", description = "특정 아이디어의 리뷰 목록을 조회합니다.")
    @GetMapping("/idea/{ideaId}")
    public ApiResponse<Page<ReviewResponse>> getReviewsByIdea(
            @PathVariable Long ideaId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewResponse> response = reviewService.getReviewsByIdea(ideaId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "내 리뷰 목록", description = "내가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<Page<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<ReviewResponse> response = reviewService.getReviewsByUser(userId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "리뷰 작성", description = "완료한 프로젝트에 리뷰를 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewResponse> createReview(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ReviewRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        ReviewResponse response = reviewService.createReview(userId, request);
        return ApiResponse.success("리뷰가 작성되었습니다.", response);
    }

    @Operation(summary = "리뷰 수정", description = "내 리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        ReviewResponse response = reviewService.updateReview(userId, reviewId, request);
        return ApiResponse.success("리뷰가 수정되었습니다.", response);
    }

    @Operation(summary = "리뷰 삭제", description = "내 리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId) {
        Long userId = Long.parseLong(jwt.getSubject());
        reviewService.deleteReview(userId, reviewId);
        return ApiResponse.success("리뷰가 삭제되었습니다.", null);
    }

    @Operation(summary = "리뷰 도움됨", description = "리뷰에 '도움됨'을 누릅니다.")
    @PostMapping("/{reviewId}/like")
    public ApiResponse<Void> likeReview(@PathVariable Long reviewId) {
        reviewService.likeReview(reviewId);
        return ApiResponse.success("도움됨을 눌렀습니다.", null);
    }

    @Operation(summary = "인기 리뷰", description = "특정 아이디어의 인기 리뷰를 조회합니다.")
    @GetMapping("/idea/{ideaId}/top")
    public ApiResponse<List<ReviewResponse>> getTopLikedReviews(
            @PathVariable Long ideaId,
            @RequestParam(defaultValue = "5") int limit) {
        List<ReviewResponse> response = reviewService.getTopLikedReviews(ideaId, limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "최근 리뷰", description = "최근 작성된 리뷰 목록을 조회합니다.")
    @GetMapping("/recent")
    public ApiResponse<List<ReviewResponse>> getRecentReviews(
            @RequestParam(defaultValue = "10") int limit) {
        List<ReviewResponse> response = reviewService.getRecentReviews(limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "리뷰 통계", description = "특정 아이디어의 리뷰 통계를 조회합니다.")
    @GetMapping("/idea/{ideaId}/stats")
    public ApiResponse<ReviewService.ReviewStats> getReviewStats(@PathVariable Long ideaId) {
        ReviewService.ReviewStats response = reviewService.getReviewStats(ideaId);
        return ApiResponse.success(response);
    }
}