package com.devroller.domain.idea.repository;

import com.devroller.domain.idea.entity.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 아이디어 Repository
 */
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    // 활성 아이디어만 조회
    List<Idea> findByIsActiveTrue();

    // 카테고리별 조회
    List<Idea> findByCategoryIdAndIsActiveTrue(Long categoryId);

    // 난이도별 조회
    List<Idea> findByDifficultyAndIsActiveTrue(Idea.Difficulty difficulty);

    // 카테고리 + 난이도 조회
    List<Idea> findByCategoryIdAndDifficultyAndIsActiveTrue(Long categoryId, Idea.Difficulty difficulty);

    // 페이징 조회
    Page<Idea> findByIsActiveTrue(Pageable pageable);

    // 카테고리별 페이징
    Page<Idea> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);

    // 제목 검색
    Page<Idea> findByTitleContainingIgnoreCaseAndIsActiveTrue(String keyword, Pageable pageable);

    // 인기 아이디어 (추첨 횟수순)
    @Query("SELECT i FROM Idea i WHERE i.isActive = true ORDER BY i.pickCount DESC")
    List<Idea> findPopularIdeas(Pageable pageable);

    // 평점 높은 아이디어
    @Query("SELECT i FROM Idea i WHERE i.isActive = true AND i.averageRating > 0 ORDER BY i.averageRating DESC")
    List<Idea> findTopRatedIdeas(Pageable pageable);

    // 최근 완료가 많은 아이디어
    @Query("SELECT i FROM Idea i WHERE i.isActive = true ORDER BY i.completedCount DESC")
    List<Idea> findMostCompletedIdeas(Pageable pageable);

    // 사용자가 아직 진행/완료하지 않은 아이디어 (추첨 대상)
    @Query("SELECT i FROM Idea i WHERE i.isActive = true " +
           "AND i.id NOT IN (SELECT ui.idea.id FROM UserIdea ui WHERE ui.user.id = :userId)")
    List<Idea> findAvailableIdeasForUser(@Param("userId") Long userId);

    // 사용자가 아직 진행/완료하지 않은 아이디어 (카테고리 필터)
    @Query("SELECT i FROM Idea i WHERE i.isActive = true AND i.category.id = :categoryId " +
           "AND i.id NOT IN (SELECT ui.idea.id FROM UserIdea ui WHERE ui.user.id = :userId)")
    List<Idea> findAvailableIdeasForUserByCategory(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    // 사용자가 아직 진행/완료하지 않은 아이디어 (난이도 필터)
    @Query("SELECT i FROM Idea i WHERE i.isActive = true AND i.difficulty = :difficulty " +
           "AND i.id NOT IN (SELECT ui.idea.id FROM UserIdea ui WHERE ui.user.id = :userId)")
    List<Idea> findAvailableIdeasForUserByDifficulty(@Param("userId") Long userId, @Param("difficulty") Idea.Difficulty difficulty);

    // 사용자가 아직 진행/완료하지 않은 아이디어 (카테고리 + 난이도 필터)
    @Query("SELECT i FROM Idea i WHERE i.isActive = true " +
           "AND i.category.id = :categoryId AND i.difficulty = :difficulty " +
           "AND i.id NOT IN (SELECT ui.idea.id FROM UserIdea ui WHERE ui.user.id = :userId)")
    List<Idea> findAvailableIdeasForUserByCategoryAndDifficulty(
            @Param("userId") Long userId, 
            @Param("categoryId") Long categoryId,
            @Param("difficulty") Idea.Difficulty difficulty);

    // 카테고리별 아이디어 수
    @Query("SELECT COUNT(i) FROM Idea i WHERE i.category.id = :categoryId AND i.isActive = true")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    // 난이도별 아이디어 수
    @Query("SELECT COUNT(i) FROM Idea i WHERE i.difficulty = :difficulty AND i.isActive = true")
    long countByDifficulty(@Param("difficulty") Idea.Difficulty difficulty);
}