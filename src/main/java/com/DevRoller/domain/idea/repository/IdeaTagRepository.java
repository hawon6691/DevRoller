package com.devroller.domain.idea.repository;

import com.devroller.domain.idea.entity.IdeaTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 아이디어-태그 연결 Repository
 */
public interface IdeaTagRepository extends JpaRepository<IdeaTag, Long> {

    // 아이디어의 모든 태그 연결 조회
    List<IdeaTag> findByIdeaId(Long ideaId);

    // 태그로 연결된 아이디어 태그 조회
    List<IdeaTag> findByTagId(Long tagId);

    // 아이디어-태그 연결 존재 여부
    boolean existsByIdeaIdAndTagId(Long ideaId, Long tagId);

    // 아이디어의 모든 태그 연결 삭제
    @Modifying
    @Query("DELETE FROM IdeaTag it WHERE it.idea.id = :ideaId")
    void deleteByIdeaId(@Param("ideaId") Long ideaId);

    // 특정 태그 연결 삭제
    @Modifying
    @Query("DELETE FROM IdeaTag it WHERE it.idea.id = :ideaId AND it.tag.id = :tagId")
    void deleteByIdeaIdAndTagId(@Param("ideaId") Long ideaId, @Param("tagId") Long tagId);
}