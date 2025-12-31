package com.devroller.domain.user.repository;

import com.devroller.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Optional<User> findByNickname(String nickname);
    
    boolean existsByEmail(String email);
    
    boolean existsByNickname(String nickname);

    // 레벨 기준 랭킹
    Page<User> findByStatusOrderByLevelDescExperienceDesc(User.UserStatus status, Pageable pageable);
    
    // 완료 프로젝트 기준 랭킹
    Page<User> findByStatusOrderByTotalCompletedDesc(User.UserStatus status, Pageable pageable);
    
    // 스트릭 기준 랭킹
    Page<User> findByStatusOrderByCurrentStreakDesc(User.UserStatus status, Pageable pageable);
    
    // 스트릭이 있는 사용자 조회
    List<User> findByCurrentStreakGreaterThan(int streak);
    
    // 특정 레벨 이상 사용자 수
    @Query("SELECT COUNT(u) FROM User u WHERE u.level >= :level AND u.status = :status")
    long countByLevelGreaterThanEqualAndStatus(@Param("level") int level, @Param("status") User.UserStatus status);
    
    // 이메일 또는 닉네임 검색
    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% OR u.nickname LIKE %:keyword%")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // 상위 N명 조회 (레벨 기준)
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' ORDER BY u.level DESC, u.experience DESC")
    List<User> findTopByLevel(Pageable pageable);
    
    // 상위 N명 조회 (완료 기준)
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' ORDER BY u.totalCompleted DESC")
    List<User> findTopByCompleted(Pageable pageable);
}
