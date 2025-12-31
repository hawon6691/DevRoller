package com.devroller.domain.gamification.streak.repository;

import com.devroller.domain.gamification.streak.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 스트릭(연속 기록) Repository
 */
public interface StreakRepository extends JpaRepository<Streak, Long> {

    // 사용자 + 날짜로 조회
    Optional<Streak> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);

    // 사용자 + 날짜 + 활동타입으로 조회
    Optional<Streak> findByUserIdAndActivityDateAndActivityType(
            Long userId, LocalDate activityDate, Streak.ActivityType activityType);

    // 사용자의 특정 기간 스트릭 조회
    @Query("SELECT s FROM Streak s WHERE s.user.id = :userId " +
           "AND s.activityDate BETWEEN :startDate AND :endDate ORDER BY s.activityDate ASC")
    List<Streak> findByUserIdAndActivityDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 사용자의 최근 N일 스트릭
    @Query("SELECT s FROM Streak s WHERE s.user.id = :userId ORDER BY s.activityDate DESC")
    List<Streak> findRecentStreaks(@Param("userId") Long userId, 
            org.springframework.data.domain.Pageable pageable);

    // 특정 날짜에 활동이 있었는지
    boolean existsByUserIdAndActivityDate(Long userId, LocalDate activityDate);

    // 사용자의 총 활동 일수
    @Query("SELECT COUNT(DISTINCT s.activityDate) FROM Streak s WHERE s.user.id = :userId")
    long countDistinctActivityDaysByUserId(@Param("userId") Long userId);

    // 사용자의 활동 타입별 총 횟수
    @Query("SELECT SUM(s.count) FROM Streak s WHERE s.user.id = :userId AND s.activityType = :type")
    Long sumCountByUserIdAndActivityType(@Param("userId") Long userId, @Param("type") Streak.ActivityType type);

    // 연속 스트릭 계산용 - 최근 연속 활동일 조회
    @Query("SELECT s.activityDate FROM Streak s WHERE s.user.id = :userId " +
           "AND s.activityDate <= :today ORDER BY s.activityDate DESC")
    List<LocalDate> findActivityDatesByUserIdOrderByDateDesc(
            @Param("userId") Long userId, @Param("today") LocalDate today);
}