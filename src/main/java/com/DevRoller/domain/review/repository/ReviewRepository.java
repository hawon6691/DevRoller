package com.devroller.domain.review.repository;

import com.devroller.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserIdAndIdeaId(Long userId, Long ideaId);
    
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);
    
    // 아이디어별 리뷰 목록
    Page<Review> findByIdeaIdOrderByCreatedAtDesc(Long ideaId, Pageable pageable);
    
    // 사용자별 리뷰 목록
    Page<Review> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 아이디어 평균 평점
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.idea.id = :ideaId")
    Double getAverageRatingByIdeaId(@Param("ideaId") Long ideaId);
    
    // 아이디어 리뷰 수
    @Query("SELECT COUNT(r) FROM Review r WHERE r.idea.id = :ideaId")
    int countByIdeaId(@Param("ideaId") Long ideaId);
    
    // 인기 리뷰 (좋아요 순)
    @Query("SELECT r FROM Review r WHERE r.idea.id = :ideaId ORDER BY r.likeCount DESC")
    List<Review> findTopReviewsByIdeaId(@Param("ideaId") Long ideaId, Pageable pageable);
    
    // 사용자의 리뷰 수
    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);
}
