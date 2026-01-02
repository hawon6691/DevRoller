package com.devroller.domain.like.repository;

import com.devroller.domain.like.entity.IdeaLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaLikeRepository extends JpaRepository<IdeaLike, Long> {

    // 사용자+아이디어 조회
    Optional<IdeaLike> findByUserIdAndIdeaId(Long userId, Long ideaId);

    // 존재 여부
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);

    // 사용자가 좋아요한 아이디어 목록
    List<IdeaLike> findByUserId(Long userId);

    // 아이디어의 좋아요 수
    @Query("SELECT COUNT(il) FROM IdeaLike il WHERE il.idea.id = :ideaId")
    long countByIdeaId(@Param("ideaId") Long ideaId);

    // 아이디어별 좋아요 수 (벌크 조회)
    @Query("SELECT il.idea.id, COUNT(il) FROM IdeaLike il WHERE il.idea.id IN :ideaIds GROUP BY il.idea.id")
    List<Object[]> countByIdeaIds(@Param("ideaIds") List<Long> ideaIds);

    // 삭제
    void deleteByUserIdAndIdeaId(Long userId, Long ideaId);
}
