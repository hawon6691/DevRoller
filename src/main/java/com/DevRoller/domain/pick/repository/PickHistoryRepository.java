package com.devroller.domain.pick.repository;

import com.devroller.domain.pick.entity.PickHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 추첨 기록 Repository
 */
public interface PickHistoryRepository extends JpaRepository<PickHistory, Long> {

    // 사용자의 추첨 기록 조회
    List<PickHistory> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 사용자의 추첨 기록 (페이징)
    Page<PickHistory> findByUserId(Long userId, Pageable pageable);

    // 추첨 방식별 기록
    List<PickHistory> findByUserIdAndPickMethod(Long userId, PickHistory.PickMethod pickMethod);

    // 사용자의 총 추첨 횟수
    long countByUserId(Long userId);

    // 추첨 방식별 통계
    @Query("SELECT ph.pickMethod, COUNT(ph) FROM PickHistory ph WHERE ph.user.id = :userId GROUP BY ph.pickMethod")
    List<Object[]> countByUserIdGroupByPickMethod(@Param("userId") Long userId);

    // 특정 기간 내 추첨 기록
    @Query("SELECT ph FROM PickHistory ph WHERE ph.user.id = :userId " +
           "AND ph.createdAt BETWEEN :startDate AND :endDate ORDER BY ph.createdAt DESC")
    List<PickHistory> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 가장 많이 추첨된 아이디어 (전체)
    @Query("SELECT ph.idea.id, COUNT(ph) as cnt FROM PickHistory ph GROUP BY ph.idea.id ORDER BY cnt DESC")
    List<Object[]> findMostPickedIdeas(Pageable pageable);

    // 오늘 추첨 횟수
    @Query("SELECT COUNT(ph) FROM PickHistory ph WHERE ph.user.id = :userId " +
           "AND ph.createdAt >= :startOfDay")
    long countTodayPicks(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay);
}