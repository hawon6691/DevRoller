package com.devroller.domain.review.repository;

import com.devroller.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 Repository
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 사용자 + 아이디어로 조회
    Optional<Review> findByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자 + 아이디어 존재 여부
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자의 모든 리뷰
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 사용자의 리뷰 (페이징)
    Page<Review> findByUserId(Long userId, Pageable pageable);

    // 아이디어의 모든 리뷰
    List<Review> findByIdeaIdOrderByCreatedAtDesc(Long ideaId);

    // 아이디어의 리뷰 (페이징)
    Page<Review> findByIdeaId(Long ideaId, Pageable pageable);

    // 아이디어의 리뷰 수
    long countByIdeaId(Long ideaId);

    // 아이디어의 평균 평점
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.idea.id = :ideaId")
    Double calculateAverageRatingByIdeaId(@Param("ideaId") Long ideaId);

    // 아이디어의 평점 분포
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.idea.id = :ideaId GROUP BY r.rating ORDER BY r.rating")
    List<Object[]> countByIdeaIdGroupByRating(@Param("ideaId") Long ideaId);

    // 난이도 피드백 분포
    @Query("SELECT r.difficultyFeedback, COUNT(r) FROM Review r WHERE r.idea.id = :ideaId " +
           "AND r.difficultyFeedback IS NOT NULL GROUP BY r.difficultyFeedback")
    List<Object[]> countByIdeaIdGroupByDifficultyFeedback(@Param("ideaId") Long ideaId);

    // 평균 실제 소요 시간
    @Query("SELECT AVG(r.actualHours) FROM Review r WHERE r.idea.id = :ideaId AND r.actualHours > 0")
    Double calculateAverageActualHoursByIdeaId(@Param("ideaId") Long ideaId);

    // 좋아요 많은 리뷰 (특정 아이디어)
    @Query("SELECT r FROM Review r WHERE r.idea.id = :ideaId ORDER BY r.likeCount DESC")
    List<Review> findTopLikedReviewsByIdeaId(@Param("ideaId") Long ideaId, Pageable pageable);

    // 최근 리뷰 (전체)
    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    List<Review> findRecentReviews(Pageable pageable);

    // 사용자의 리뷰 수
    long countByUserId(Long userId);
}