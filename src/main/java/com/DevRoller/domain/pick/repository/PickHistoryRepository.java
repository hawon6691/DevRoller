package com.devroller.domain.pick.repository;

import com.devroller.domain.pick.entity.PickHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PickHistoryRepository extends JpaRepository<PickHistory, Long> {

    // 사용자의 추첨 기록 조회 (최신순)
    Page<PickHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 사용자의 최근 N개 추첨 기록
    List<PickHistory> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    
    // 특정 기간 내 추첨 기록
    @Query("SELECT ph FROM PickHistory ph WHERE ph.user.id = :userId " +
           "AND ph.createdAt BETWEEN :start AND :end ORDER BY ph.createdAt DESC")
    List<PickHistory> findByUserIdAndPeriod(
        @Param("userId") Long userId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    // 추첨 방식별 통계
    @Query("SELECT ph.pickMethod, COUNT(ph) FROM PickHistory ph " +
           "WHERE ph.user.id = :userId GROUP BY ph.pickMethod")
    List<Object[]> countByPickMethod(@Param("userId") Long userId);
    
    // 사용자의 총 추첨 횟수
    long countByUserId(Long userId);
    
    // 특정 아이디어가 추첨된 횟수
    long countByIdeaId(Long ideaId);
}
