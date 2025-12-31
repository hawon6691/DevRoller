package com.devroller.domain.gamification.title.repository;

import com.devroller.domain.gamification.title.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 칭호 Repository
 */
public interface TitleRepository extends JpaRepository<Title, Long> {

    // 코드로 칭호 조회
    Optional<Title> findByCode(String code);

    // 코드 존재 여부
    boolean existsByCode(String code);

    // 타입별 칭호 조회
    List<Title> findByTypeOrderByDisplayOrderAsc(Title.TitleType type);

    // 레벨 기반 칭호 조회 (해당 레벨 이하)
    List<Title> findByTypeAndRequiredLevelLessThanEqualOrderByRequiredLevelDesc(
            Title.TitleType type, Integer level);

    // 특정 레벨에서 획득 가능한 칭호
    Optional<Title> findByTypeAndRequiredLevel(Title.TitleType type, Integer level);

    // 희귀도별 칭호 조회
    List<Title> findByRarityOrderByDisplayOrderAsc(Title.Rarity rarity);

    // 전체 칭호 (정렬)
    List<Title> findAllByOrderByDisplayOrderAsc();

    // 특정 업적 코드로 획득 가능한 칭호
    Optional<Title> findByRequiredAchievementCode(String achievementCode);
}