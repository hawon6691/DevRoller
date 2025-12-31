package com.devroller.domain.review.service;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.UserIdea;
import com.devroller.domain.idea.repository.UserIdeaRepository;
import com.devroller.domain.idea.service.IdeaService;
import com.devroller.domain.review.dto.ReviewRequest;
import com.devroller.domain.review.dto.ReviewResponse;
import com.devroller.domain.review.entity.Review;
import com.devroller.domain.review.repository.ReviewRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserIdeaRepository userIdeaRepository;
    private final UserService userService;
    private final IdeaService ideaService;

    /**
     * 아이디어의 리뷰 목록
     */
    public Page<ReviewResponse> getReviewsByIdea(Long ideaId, Pageable pageable) {
        return reviewRepository.findByIdeaId(ideaId, pageable)
                .map(ReviewResponse::from);
    }

    /**
     * 사용자의 리뷰 목록
     */
    public Page<ReviewResponse> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewResponse::from);
    }

    /**
     * 리뷰 작성
     */
    @Transactional
    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        // 완료한 프로젝트인지 확인
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, request.getIdeaId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (userIdea.getStatus() != UserIdea.Status.COMPLETED) {
            throw new BusinessException(ErrorCode.NOT_COMPLETED, "완료한 프로젝트만 리뷰할 수 있습니다.");
        }

        // 이미 리뷰한 경우
        if (reviewRepository.existsByUserIdAndIdeaId(userId, request.getIdeaId())) {
            throw new BusinessException(ErrorCode.ALREADY_REVIEWED);
        }

        User user = userService.findById(userId);
        Idea idea = ideaService.findById(request.getIdeaId());

        Review review = Review.builder()
                .user(user)
                .idea(idea)
                .rating(request.getRating())
                .content(request.getContent())
                .actualHours(request.getActualHours())
                .difficultyFeedback(request.getDifficultyFeedback())
                .build();

        Review saved = reviewRepository.save(review);

        // 평균 평점 업데이트
        updateAverageRating(request.getIdeaId());

        log.info("User {} reviewed idea {} (rating: {})", userId, request.getIdeaId(), request.getRating());

        return ReviewResponse.from(saved);
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewResponse updateReview(Long userId, Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 리뷰만 수정할 수 있습니다.");
        }

        review.update(
                request.getRating(),
                request.getContent(),
                request.getActualHours(),
                request.getDifficultyFeedback()
        );

        // 평균 평점 업데이트
        updateAverageRating(review.getIdea().getId());

        return ReviewResponse.from(review);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 리뷰만 삭제할 수 있습니다.");
        }

        Long ideaId = review.getIdea().getId();
        reviewRepository.delete(review);

        // 평균 평점 업데이트
        updateAverageRating(ideaId);

        log.info("User {} deleted review {}", userId, reviewId);
    }

    /**
     * 리뷰 도움됨 증가
     */
    @Transactional
    public void likeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        review.incrementLikeCount();
    }

    /**
     * 평균 평점 업데이트
     */
    private void updateAverageRating(Long ideaId) {
        Double average = reviewRepository.calculateAverageRatingByIdeaId(ideaId);
        if (average != null) {
            ideaService.updateAverageRating(ideaId, Math.round(average * 10) / 10.0);
        } else {
            ideaService.updateAverageRating(ideaId, 0.0);
        }
    }

    /**
     * 인기 리뷰 (좋아요 많은)
     */
    public List<ReviewResponse> getTopLikedReviews(Long ideaId, int limit) {
        return reviewRepository.findTopLikedReviewsByIdeaId(ideaId, PageRequest.of(0, limit))
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 최근 리뷰 (전체)
     */
    public List<ReviewResponse> getRecentReviews(int limit) {
        return reviewRepository.findRecentReviews(PageRequest.of(0, limit))
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 통계
     */
    public ReviewStats getReviewStats(Long ideaId) {
        long count = reviewRepository.countByIdeaId(ideaId);
        Double average = reviewRepository.calculateAverageRatingByIdeaId(ideaId);
        Double avgHours = reviewRepository.calculateAverageActualHoursByIdeaId(ideaId);

        return new ReviewStats(count, average != null ? average : 0.0, avgHours != null ? avgHours : 0.0);
    }

    public record ReviewStats(long count, double averageRating, double averageHours) {}
}