package com.devroller.domain.idea.repository;

import com.devroller.domain.idea.entity.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    // 활성화된 아이디어 전체 조회
    List<Idea> findByIsActiveTrue();
    
    // 활성화된 아이디어 페이징 조회
    Page<Idea> findByIsActiveTrue(Pageable pageable);
    
    // 카테고리별 조회
    Page<Idea> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
    
    // 난이도별 조회
    List<Idea> findByDifficultyAndIsActiveTrue(Idea.Difficulty difficulty);
    
    Page<Idea> findByDifficultyAndIsActiveTrue(Idea.Difficulty difficulty, Pageable pageable);
    
    // 카테고리 + 난이도 조회
    List<Idea> findByCategoryIdAndDifficultyAndIsActiveTrue(Long categoryId, Idea.Difficulty difficulty);
    
    // 인기순 조회
    @Query("SELECT i FROM Idea i WHERE i.isActive = true ORDER BY i.pickCount DESC")
    Page<Idea> findPopularIdeas(Pageable pageable);
    
    // 평점 높은순 조회
    @Query("SELECT i FROM Idea i WHERE i.isActive = true ORDER BY i.averageRating DESC")
    Page<Idea> findTopRatedIdeas(Pageable pageable);
    
    // 최신순 조회
    Page<Idea> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // 키워드 검색
    @Query("SELECT i FROM Idea i WHERE i.isActive = true AND " +
           "(LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Idea> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // 태그로 검색
    @Query("SELECT DISTINCT i FROM Idea i JOIN i.ideaTags it " +
           "WHERE i.isActive = true AND it.tag.id = :tagId")
    Page<Idea> findByTagId(@Param("tagId") Long tagId, Pageable pageable);
    
    // 랜덤 N개 조회
    @Query(value = "SELECT * FROM ideas WHERE is_active = true ORDER BY RAND() LIMIT :count", 
           nativeQuery = true)
    List<Idea> findRandomIdeas(@Param("count") int count);
    
    // 카테고리 내 랜덤 N개
    @Query(value = "SELECT * FROM ideas WHERE is_active = true AND category_id = :categoryId ORDER BY RAND() LIMIT :count",
           nativeQuery = true)
    List<Idea> findRandomIdeasByCategory(@Param("categoryId") Long categoryId, @Param("count") int count);
}
