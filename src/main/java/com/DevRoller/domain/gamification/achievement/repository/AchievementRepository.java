package com.devroller.domain.gamification.achievement.repository;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 업적 Repository
 */
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    // 코드로 업적 조회
    Optional<Achievement> findByCode(String code);

    // 코드 존재 여부
    boolean existsByCode(String code);

    // 타입별 업적 조회
    List<Achievement> findByTypeOrderByDisplayOrderAsc(Achievement.AchievementType type);

    // 히든 업적 제외 조회
    List<Achievement> findByIsHiddenFalseOrderByDisplayOrderAsc();

    // 전체 업적 (정렬)
    List<Achievement> findAllByOrderByDisplayOrderAsc();

    // 보상 경험치가 있는 업적만
    List<Achievement> findByRewardExpGreaterThanOrderByDisplayOrderAsc(int exp);
}