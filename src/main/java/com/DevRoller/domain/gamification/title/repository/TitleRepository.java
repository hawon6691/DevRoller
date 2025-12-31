package com.devroller.domain.gamification.title.repository;

import com.devroller.domain.gamification.title.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

    Optional<Title> findByCode(String code);
    
    boolean existsByCode(String code);
    
    // 타입별 칭호 조회
    List<Title> findByTypeOrderByDisplayOrderAsc(Title.TitleType type);
    
    // 레벨 기반 칭호 조회 (특정 레벨 이하)
    List<Title> findByTypeAndRequiredLevelLessThanEqual(Title.TitleType type, int level);
    
    // 업적 연동 칭호 조회
    Optional<Title> findByRequiredAchievementCode(String achievementCode);
    
    // 희귀도별 칭호 조회
    List<Title> findByRarityOrderByDisplayOrderAsc(Title.Rarity rarity);
    
    // 전체 칭호 조회 (표시순)
    List<Title> findAllByOrderByDisplayOrderAsc();
    
    // 특정 레벨에서 획득 가능한 칭호 조회
    List<Title> findByTypeAndRequiredLevel(Title.TitleType type, int level);
}
