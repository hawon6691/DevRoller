package com.devroller.domain.gamification.achievement.repository;

import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 업적 Repository
 */
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    // 사용자 + 업적으로 조회
    Optional<UserAchievement> findByUserIdAndAchievementId(Long userId, Long achievementId);

    // 사용자 + 업적 코드로 조회
    @Query("SELECT ua FROM UserAchievement ua JOIN ua.achievement a WHERE ua.user.id = :userId AND a.code = :code")
    Optional<UserAchievement> findByUserIdAndAchievementCode(@Param("userId") Long userId, @Param("code") String code);

    // 사용자의 모든 업적 조회
    List<UserAchievement> findByUserId(Long userId);

    // 사용자의 완료된 업적만
    List<UserAchievement> findByUserIdAndIsCompletedTrue(Long userId);

    // 사용자의 진행중인 업적
    List<UserAchievement> findByUserIdAndIsCompletedFalse(Long userId);

    // 사용자 + 업적 존재 여부
    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);

    // 사용자의 완료된 업적 수
    long countByUserIdAndIsCompletedTrue(Long userId);

    // 최근 달성한 업적
    @Query("SELECT ua FROM UserAchievement ua WHERE ua.user.id = :userId AND ua.isCompleted = true " +
           "ORDER BY ua.achievedAt DESC")
    List<UserAchievement> findRecentAchievements(@Param("userId") Long userId, 
            org.springframework.data.domain.Pageable pageable);

    // 특정 업적을 달성한 사용자 수
    @Query("SELECT COUNT(ua) FROM UserAchievement ua WHERE ua.achievement.id = :achievementId AND ua.isCompleted = true")
    long countCompletedByAchievementId(@Param("achievementId") Long achievementId);
}