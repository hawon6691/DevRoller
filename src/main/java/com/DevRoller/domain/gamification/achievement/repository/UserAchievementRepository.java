package com.devroller.domain.gamification.achievement.repository;

import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    // 사용자+업적 조합 조회
    Optional<UserAchievement> findByUserIdAndAchievementId(Long userId, Long achievementId);
    
    // 사용자+업적 존재 여부
    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
    
    // 사용자의 전체 업적 조회
    List<UserAchievement> findByUserId(Long userId);
    
    // 사용자의 완료된 업적만 조회
    List<UserAchievement> findByUserIdAndIsCompletedTrue(Long userId);
    
    // 사용자의 진행 중인 업적 조회
    List<UserAchievement> findByUserIdAndIsCompletedFalse(Long userId);
    
    // 사용자의 완료된 업적 수
    @Query("SELECT COUNT(ua) FROM UserAchievement ua WHERE ua.user.id = :userId AND ua.isCompleted = true")
    int countCompletedByUserId(@Param("userId") Long userId);

    long countByUserIdAndIsCompletedTrue(Long userId);

    // 최근 달성 업적 조회
    @Query("SELECT ua FROM UserAchievement ua " +
           "WHERE ua.user.id = :userId AND ua.isCompleted = true " +
           "ORDER BY ua.achievedAt DESC")
    List<UserAchievement> findRecentlyCompleted(@Param("userId") Long userId);

    @Query("SELECT ua FROM UserAchievement ua " +
           "WHERE ua.user.id = :userId AND ua.isCompleted = true " +
           "ORDER BY ua.achievedAt DESC")
    org.springframework.data.domain.Page<UserAchievement> findRecentAchievements(
            @Param("userId") Long userId,
            org.springframework.data.domain.Pageable pageable);

    // 특정 업적을 달성한 사용자 수
    @Query("SELECT COUNT(ua) FROM UserAchievement ua " +
           "WHERE ua.achievement.id = :achievementId AND ua.isCompleted = true")
    int countUsersWithAchievement(@Param("achievementId") Long achievementId);
}
