package com.devroller.domain.idea.repository;

import com.devroller.domain.idea.entity.UserIdea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 사용자-아이디어 상태 Repository
 */
public interface UserIdeaRepository extends JpaRepository<UserIdea, Long> {

    // 사용자 + 아이디어로 조회
    Optional<UserIdea> findByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자 + 아이디어 존재 여부
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자의 모든 프로젝트 조회
    List<UserIdea> findByUserId(Long userId);

    // 사용자의 상태별 프로젝트 조회
    List<UserIdea> findByUserIdAndStatus(Long userId, UserIdea.Status status);

    // 사용자의 진행중인 프로젝트
    List<UserIdea> findByUserIdAndStatusOrderByStartedAtDesc(Long userId, UserIdea.Status status);

    // 사용자의 완료된 프로젝트 (페이징)
    Page<UserIdea> findByUserIdAndStatus(Long userId, UserIdea.Status status, Pageable pageable);

    // 사용자의 프로젝트 수 (상태별)
    long countByUserIdAndStatus(Long userId, UserIdea.Status status);

    // 사용자의 총 완료 프로젝트 수
    @Query("SELECT COUNT(ui) FROM UserIdea ui WHERE ui.user.id = :userId AND ui.status = 'COMPLETED'")
    long countCompletedByUserId(@Param("userId") Long userId);

    // 특정 기간 내 완료된 프로젝트
    @Query("SELECT ui FROM UserIdea ui WHERE ui.user.id = :userId AND ui.status = 'COMPLETED' " +
           "AND ui.completedAt BETWEEN :startDate AND :endDate")
    List<UserIdea> findCompletedBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 아이디어별 완료 횟수
    @Query("SELECT COUNT(ui) FROM UserIdea ui WHERE ui.idea.id = :ideaId AND ui.status = 'COMPLETED'")
    long countCompletedByIdeaId(@Param("ideaId") Long ideaId);

    // 최근 완료한 프로젝트
    @Query("SELECT ui FROM UserIdea ui WHERE ui.user.id = :userId AND ui.status = 'COMPLETED' " +
           "ORDER BY ui.completedAt DESC")
    List<UserIdea> findRecentCompleted(@Param("userId") Long userId, Pageable pageable);
}