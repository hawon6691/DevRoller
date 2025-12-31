package com.devroller.domain.gamification.streak.repository;

import com.devroller.domain.gamification.streak.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {

    // 특정 날짜/타입의 스트릭 조회
    Optional<Streak> findByUserIdAndActivityDateAndActivityType(
        Long userId, LocalDate activityDate, Streak.ActivityType activityType
    );
    
    // 사용자의 특정 기간 활동 조회
    List<Streak> findByUserIdAndActivityDateBetweenOrderByActivityDateDesc(
        Long userId, LocalDate startDate, LocalDate endDate
    );
    
    // 특정 날짜/타입 활동 존재 여부
    boolean existsByUserIdAndActivityDateAndActivityType(
        Long userId, LocalDate activityDate, Streak.ActivityType activityType
    );
    
    // 사용자의 최근 활동일 조회
    @Query("SELECT MAX(s.activityDate) FROM Streak s WHERE s.user.id = :userId AND s.activityType = :type")
    Optional<LocalDate> findLastActivityDate(
        @Param("userId") Long userId, 
        @Param("type") Streak.ActivityType type
    );
    
    // 사용자의 특정 타입 전체 활동 횟수
    @Query("SELECT COALESCE(SUM(s.count), 0) FROM Streak s WHERE s.user.id = :userId AND s.activityType = :type")
    int countTotalActivities(
        @Param("userId") Long userId, 
        @Param("type") Streak.ActivityType type
    );
    
    // 오래된 스트릭 기록 삭제
    @Modifying
    @Query("DELETE FROM Streak s WHERE s.activityDate < :cutoffDate")
    int deleteByActivityDateBefore(@Param("cutoffDate") LocalDate cutoffDate);
    
    // 사용자별 활동 타입별 통계
    @Query("SELECT s.activityType, COUNT(DISTINCT s.activityDate) FROM Streak s " +
           "WHERE s.user.id = :userId GROUP BY s.activityType")
    List<Object[]> countActiveDaysByType(@Param("userId") Long userId);
    
    // 특정 기간 내 활동일 수
    @Query("SELECT COUNT(DISTINCT s.activityDate) FROM Streak s " +
           "WHERE s.user.id = :userId AND s.activityType = :type " +
           "AND s.activityDate BETWEEN :startDate AND :endDate")
    int countActiveDaysInPeriod(
        @Param("userId") Long userId,
        @Param("type") Streak.ActivityType type,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
