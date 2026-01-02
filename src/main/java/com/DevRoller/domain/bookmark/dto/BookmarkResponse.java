package com.devroller.domain.bookmark.dto;

import com.devroller.domain.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookmarkResponse {

    private Long id;
    private Long ideaId;
    private String ideaTitle;
    private String ideaDescription;
    private String categoryName;
    private String difficulty;
    private String memo;
    private LocalDateTime createdAt;

    public static BookmarkResponse from(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .ideaId(bookmark.getIdea().getId())
                .ideaTitle(bookmark.getIdea().getTitle())
                .ideaDescription(bookmark.getIdea().getDescription())
                .categoryName(bookmark.getIdea().getCategory().getName())
                .difficulty(bookmark.getIdea().getDifficulty().name())
                .memo(bookmark.getMemo())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
