package com.devroller.domain.review.service;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.UserIdea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.idea.repository.UserIdeaRepository;
import com.devroller.domain.review.dto.ReviewRequest;
import com.devroller.domain.review.dto.ReviewResponse;
import com.devroller.domain.review.entity.Review;
import com.devroller.domain.review.repository.ReviewRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final UserIdeaRepository userIdeaRepository;

    /**
     * 리뷰 작성
     */
    @Transactional
    public ReviewResponse createReview(Long userId, Long ideaId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));

        // 완료한 프로젝트인지 확인
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        
        if (userIdea.getStatus() != UserIdea.Status.COMPLETED) {
            throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOWED);
        }

        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_REVIEWED);
        }

        Review review = Review.builder()
                .user(user)
                .idea(idea)
                .rating(request.getRating())
                .content(request.getContent())
                .actualHours(request.getActualHours())
                .difficultyFeedback(request.getDifficultyFeedback())
                .build();

        reviewRepository.save(review);

        // 아이디어 평균 평점 업데이트
        updateIdeaRating(idea);

        return ReviewResponse.from(review);
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewResponse updateReview(Long userId, Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        review.update(
                request.getRating(),
                request.getContent(),
                request.getActualHours(),
                request.getDifficultyFeedback()
        );

        // 아이디어 평균 평점 업데이트
        updateIdeaRating(review.getIdea());

        return ReviewResponse.from(review);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Idea idea = review.getIdea();
        reviewRepository.delete(review);

        // 아이디어 평균 평점 업데이트
        updateIdeaRating(idea);
    }

    /**
     * 리뷰 좋아요
     */
    @Transactional
    public void likeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        review.incrementLikeCount();
    }

    /**
     * 리뷰 좋아요 취소
     */
    @Transactional
    public void unlikeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        review.decrementLikeCount();
    }

    /**
     * 아이디어별 리뷰 목록 조회
     */
    public Page<ReviewResponse> getReviewsByIdea(Long ideaId, Pageable pageable) {
        return reviewRepository.findByIdeaIdOrderByCreatedAtDesc(ideaId, pageable)
                .map(ReviewResponse::from);
    }

    /**
     * 내 리뷰 목록 조회
     */
    public Page<ReviewResponse> getMyReviews(Long userId, Pageable pageable) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(ReviewResponse::from);
    }

    /**
     * 리뷰 단건 조회
     */
    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        return ReviewResponse.from(review);
    }

    /**
     * 아이디어 평균 평점 업데이트
     */
    private void updateIdeaRating(Idea idea) {
        Double avgRating = reviewRepository.getAverageRatingByIdeaId(idea.getId());
        idea.updateAverageRating(avgRating != null ? avgRating : 0.0);
    }
}
