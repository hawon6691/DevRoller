package com.devroller.domain.bookmark.repository;

import com.devroller.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndIdeaId(Long userId, Long ideaId);
    
    boolean existsByUserIdAndIdeaId(Long userId, Long ideaId);
    
    Page<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    void deleteByUserIdAndIdeaId(Long userId, Long ideaId);
    
    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.idea.id = :ideaId")
    int countByIdeaId(@Param("ideaId") Long ideaId);
}
