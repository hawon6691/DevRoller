package com.devroller.domain.user.repository;

import com.devroller.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 조회
    Optional<User> findByNickname(String nickname);

    // 이메일 존재 여부
    boolean existsByEmail(String email);

    // 닉네임 존재 여부
    boolean existsByNickname(String nickname);

    // 활성 사용자만 조회
    Optional<User> findByEmailAndStatus(String email, User.UserStatus status);

    // 레벨 순 랭킹 (상위 N명)
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' ORDER BY u.level DESC, u.experience DESC")
    List<User> findTopByLevelRanking(org.springframework.data.domain.Pageable pageable);

    // 완료 프로젝트 수 순 랭킹
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' ORDER BY u.totalCompleted DESC")
    List<User> findTopByCompletedRanking(org.springframework.data.domain.Pageable pageable);

    // 스트릭 순 랭킹
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' ORDER BY u.currentStreak DESC")
    List<User> findTopByStreakRanking(org.springframework.data.domain.Pageable pageable);

    // 특정 레벨 이상 사용자 수
    @Query("SELECT COUNT(u) FROM User u WHERE u.level >= :level AND u.status = 'ACTIVE'")
    long countByLevelGreaterThanEqual(@Param("level") int level);
}