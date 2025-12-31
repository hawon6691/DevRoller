package com.devroller.domain.bookmark.repository;

import com.devroller.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 북마크 Repository
 */
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 사용자 + 아이디어로 조회
    Optional<Bookmark> findByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자 + 아이디어 존재 여부
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자의 모든 북마크 조회
    List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 사용자의 북마크 (페이징)
    Page<Bookmark> findByUserId(Long userId, Pageable pageable);

    // 사용자의 북마크 수
    long countByUserId(Long userId);

    // 아이디어의 북마크 수
    long countByIdeaId(Long ideaId);

    // 사용자 + 아이디어로 삭제
    void deleteByUserIdAndIdeaId(Long userId, Long ideaId);

    // 가장 많이 북마크된 아이디어
    @Query("SELECT b.idea.id, COUNT(b) as cnt FROM Bookmark b GROUP BY b.idea.id ORDER BY cnt DESC")
    List<Object[]> findMostBookmarkedIdeas(Pageable pageable);

    // 특정 카테고리의 북마크
    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId AND b.idea.category.id = :categoryId " +
           "ORDER BY b.createdAt DESC")
    List<Bookmark> findByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}