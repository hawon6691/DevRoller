package com.devroller.domain.gamification.achievement.repository;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByCode(String code);
    
    boolean existsByCode(String code);
    
    // 타입별 업적 조회
    List<Achievement> findByType(Achievement.AchievementType type);
    
    // 활성화된 업적만 조회
    List<Achievement> findByIsHiddenFalseOrderByDisplayOrderAsc();
    
    // 숨겨진 업적 포함 전체 조회 (관리자용)
    List<Achievement> findAllByOrderByDisplayOrderAsc();
    
    // 타입별 업적 조회 (표시순)
    List<Achievement> findByTypeOrderByDisplayOrderAsc(Achievement.AchievementType type);
    
    // 보상 경험치가 있는 업적 조회
    List<Achievement> findByRewardExpGreaterThan(int minExp);
}
