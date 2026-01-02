package com.devroller.domain.gamification.title.repository;

import com.devroller.domain.gamification.title.entity.UserTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {

    // 사용자+칭호 조합 조회
    Optional<UserTitle> findByUserIdAndTitleId(Long userId, Long titleId);
    
    // 사용자+칭호 존재 여부
    boolean existsByUserIdAndTitleId(Long userId, Long titleId);
    
    // 사용자의 전체 칭호 조회
    List<UserTitle> findByUserId(Long userId);
    
    // 사용자의 장착된 칭호 조회
    Optional<UserTitle> findByUserIdAndIsEquippedTrue(Long userId);
    
    // 사용자의 칭호 수
    @Query("SELECT COUNT(ut) FROM UserTitle ut WHERE ut.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);
    
    // 최근 획득 칭호 조회
    @Query("SELECT ut FROM UserTitle ut WHERE ut.user.id = :userId ORDER BY ut.acquiredAt DESC")
    List<UserTitle> findRecentlyAcquired(@Param("userId") Long userId);
    
    // 특정 칭호를 보유한 사용자 수
    @Query("SELECT COUNT(ut) FROM UserTitle ut WHERE ut.title.id = :titleId")
    int countUsersWithTitle(@Param("titleId") Long titleId);
    
    // 특정 희귀도의 칭호 보유 수
    @Query("SELECT COUNT(ut) FROM UserTitle ut " +
           "WHERE ut.user.id = :userId AND ut.title.rarity = :rarity")
    int countByUserIdAndRarity(
        @Param("userId") Long userId,
        @Param("rarity") com.devroller.domain.gamification.title.entity.Title.Rarity rarity
    );

    // 사용자의 칭호 목록 (획득일 내림차순)
    List<UserTitle> findByUserIdOrderByAcquiredAtDesc(Long userId);

    // 모든 칭호 장착 해제
    @Modifying
    @Query("UPDATE UserTitle ut SET ut.isEquipped = false WHERE ut.user.id = :userId")
    void unequipAllByUserId(@Param("userId") Long userId);

    // 최근 획득 칭호 (페이징)
    @Query("SELECT ut FROM UserTitle ut WHERE ut.user.id = :userId ORDER BY ut.acquiredAt DESC")
    Page<UserTitle> findRecentTitles(@Param("userId") Long userId, Pageable pageable);
}
