package com.devroller.domain.gamification.title.repository;

import com.devroller.domain.gamification.title.entity.UserTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 칭호 Repository
 */
public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {

    // 사용자 + 칭호로 조회
    Optional<UserTitle> findByUserIdAndTitleId(Long userId, Long titleId);

    // 사용자 + 칭호 존재 여부
    boolean existsByUserIdAndTitleId(Long userId, Long titleId);

    // 사용자의 모든 칭호 조회
    List<UserTitle> findByUserIdOrderByAcquiredAtDesc(Long userId);

    // 사용자의 장착된 칭호
    Optional<UserTitle> findByUserIdAndIsEquippedTrue(Long userId);

    // 사용자의 칭호 수
    long countByUserId(Long userId);

    // 칭호 장착 해제 (다른 칭호 장착 전에 호출)
    @Modifying
    @Query("UPDATE UserTitle ut SET ut.isEquipped = false WHERE ut.user.id = :userId")
    void unequipAllByUserId(@Param("userId") Long userId);

    // 특정 칭호를 보유한 사용자 수
    @Query("SELECT COUNT(ut) FROM UserTitle ut WHERE ut.title.id = :titleId")
    long countByTitleId(@Param("titleId") Long titleId);

    // 최근 획득한 칭호
    @Query("SELECT ut FROM UserTitle ut WHERE ut.user.id = :userId ORDER BY ut.acquiredAt DESC")
    List<UserTitle> findRecentTitles(@Param("userId") Long userId, 
            org.springframework.data.domain.Pageable pageable);
}