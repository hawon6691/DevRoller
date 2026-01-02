package com.devroller.domain.suggestion.repository;

import com.devroller.domain.suggestion.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    // 상태별 조회
    Page<Suggestion> findByStatusOrderByCreatedAtDesc(Suggestion.Status status, Pageable pageable);
    
    // 사용자별 조회
    Page<Suggestion> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 투표순 조회
    Page<Suggestion> findByStatusOrderByVoteCountDesc(Suggestion.Status status, Pageable pageable);
    
    // 대기 중인 제안 목록 (투표순)
    List<Suggestion> findTop10ByStatusOrderByVoteCountDesc(Suggestion.Status status);
    
    // 사용자의 제안 수
    int countByUserId(Long userId);
    
    // 상태별 제안 수
    int countByStatus(Suggestion.Status status);
}
