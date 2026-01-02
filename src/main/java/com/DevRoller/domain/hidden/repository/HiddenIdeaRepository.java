package com.devroller.domain.hidden.repository;

import com.devroller.domain.hidden.entity.HiddenIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HiddenIdeaRepository extends JpaRepository<HiddenIdea, Long> {

    // 사용자+아이디어 조회
    Optional<HiddenIdea> findByUserIdAndIdeaId(Long userId, Long ideaId);

    // 존재 여부
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자가 숨긴 아이디어 목록
    List<HiddenIdea> findByUserId(Long userId);

    // 삭제
    void deleteByUserIdAndIdeaId(Long userId, Long ideaId);
}
