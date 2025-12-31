package com.devroller.domain.suggestion.repository;

import com.devroller.domain.suggestion.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 주제 제안 Repository
 */
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    // 사용자의 제안 목록
    List<Suggestion> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 사용자의 제안 (페이징)
    Page<Suggestion> findByUserId(Long userId, Pageable pageable);

    // 상태별 제안 목록
    List<Suggestion> findByStatusOrderByCreatedAtDesc(Suggestion.Status status);

    // 상태별 제안 (페이징)
    Page<Suggestion> findByStatus(Suggestion.Status status, Pageable pageable);

    // 대기중인 제안 (투표순 정렬)
    @Query("SELECT s FROM Suggestion s WHERE s.status = 'PENDING' ORDER BY s.voteCount DESC, s.createdAt ASC")
    Page<Suggestion> findPendingOrderByVoteCountDesc(Pageable pageable);

    // 카테고리별 제안
    Page<Suggestion> findByCategoryIdAndStatus(Long categoryId, Suggestion.Status status, Pageable pageable);

    // 사용자의 상태별 제안 수
    long countByUserIdAndStatus(Long userId, Suggestion.Status status);

    // 사용자의 총 제안 수
    long countByUserId(Long userId);

    // 상태별 제안 수
    long countByStatus(Suggestion.Status status);

    // 제목 검색 (대기중인 것만)
    @Query("SELECT s FROM Suggestion s WHERE s.status = 'PENDING' " +
           "AND LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY s.voteCount DESC")
    Page<Suggestion> searchPendingByTitle(@Param("keyword") String keyword, Pageable pageable);

    // 최근 승인된 제안
    @Query("SELECT s FROM Suggestion s WHERE s.status = 'APPROVED' ORDER BY s.updatedAt DESC")
    List<Suggestion> findRecentApproved(Pageable pageable);

    // 투표수 높은 제안 (대기중)
    @Query("SELECT s FROM Suggestion s WHERE s.status = 'PENDING' ORDER BY s.voteCount DESC")
    List<Suggestion> findTopVotedPending(Pageable pageable);
}