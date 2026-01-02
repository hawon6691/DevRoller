package com.devroller.domain.gamification.streak.repository;

import com.devroller.domain.gamification.streak.entity.Streak;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 특정 기간의 스트릭 조회
    List<Streak> findByUserIdAndActivityDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 최근 스트릭 조회 (페이징)
    @Query("SELECT s FROM Streak s WHERE s.user.id = :userId ORDER BY s.activityDate DESC")
    Page<Streak> findRecentStreaks(@Param("userId") Long userId, Pageable pageable);

    // 특정 날짜 이전의 활동 날짜 조회
    @Query("SELECT DISTINCT s.activityDate FROM Streak s WHERE s.user.id = :userId AND s.activityDate <= :today ORDER BY s.activityDate DESC")
    List<LocalDate> findActivityDatesByUserIdOrderByDateDesc(@Param("userId") Long userId, @Param("today") LocalDate today);

    // 전체 활동일 수
    @Query("SELECT COUNT(DISTINCT s.activityDate) FROM Streak s WHERE s.user.id = :userId")
    long countDistinctActivityDaysByUserId(@Param("userId") Long userId);

    // 특정 타입의 활동 횟수 합계
    @Query("SELECT COALESCE(SUM(s.count), 0) FROM Streak s WHERE s.user.id = :userId AND s.activityType = :type")
    Long sumCountByUserIdAndActivityType(@Param("userId") Long userId, @Param("type") Streak.ActivityType type);

    // 특정 날짜에 활동 존재 여부
    boolean existsByUserIdAndActivityDate(Long userId, LocalDate activityDate);
}
