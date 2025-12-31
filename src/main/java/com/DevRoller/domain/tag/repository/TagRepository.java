package com.devroller.domain.tag.repository;

import com.devroller.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 태그 Repository
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    // 이름으로 태그 조회
    Optional<Tag> findByName(String name);

    // 이름 존재 여부
    boolean existsByName(String name);

    // 이름 목록으로 태그 조회
    List<Tag> findByNameIn(List<String> names);

    // 이름 검색 (부분 일치)
    List<Tag> findByNameContainingIgnoreCase(String keyword);

    // 가장 많이 사용된 태그 조회
    @Query("SELECT t FROM Tag t JOIN IdeaTag it ON t.id = it.tag.id " +
           "GROUP BY t.id ORDER BY COUNT(it) DESC")
    List<Tag> findPopularTags(org.springframework.data.domain.Pageable pageable);

    // 특정 아이디어의 태그 목록
    @Query("SELECT t FROM Tag t JOIN IdeaTag it ON t.id = it.tag.id WHERE it.idea.id = :ideaId")
    List<Tag> findByIdeaId(@Param("ideaId") Long ideaId);
}