package com.devroller.domain.idea.repository;

import com.devroller.domain.idea.entity.UserIdea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIdeaRepository extends JpaRepository<UserIdea, Long> {

    // 사용자+아이디어 조회
    Optional<UserIdea> findByUserIdAndIdeaId(Long userId, Long ideaId);
    
    // 존재 여부
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);
    
    // 사용자의 전체 프로젝트
    Page<UserIdea> findByUserId(Long userId, Pageable pageable);
    
    // 사용자의 상태별 프로젝트
    List<UserIdea> findByUserIdAndStatus(Long userId, UserIdea.Status status);
    
    Page<UserIdea> findByUserIdAndStatus(Long userId, UserIdea.Status status, Pageable pageable);
    
    // 진행 중인 프로젝트
    List<UserIdea> findByUserIdAndStatusOrderByStartedAtDesc(Long userId, UserIdea.Status status);
    
    // 완료된 프로젝트 수
    @Query("SELECT COUNT(ui) FROM UserIdea ui WHERE ui.user.id = :userId AND ui.status = 'COMPLETED'")
    int countCompletedByUserId(@Param("userId") Long userId);
    
    // 특정 아이디어를 진행 중인 사용자 수
    @Query("SELECT COUNT(ui) FROM UserIdea ui WHERE ui.idea.id = :ideaId AND ui.status = 'IN_PROGRESS'")
    int countInProgressByIdeaId(@Param("ideaId") Long ideaId);

    // 상태별 개수
    long countByUserIdAndStatus(Long userId, UserIdea.Status status);
}
