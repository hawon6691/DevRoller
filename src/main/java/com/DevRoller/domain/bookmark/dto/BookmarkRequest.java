package com.devroller.domain.bookmark.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 북마크 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkRequest {

    @NotNull(message = "아이디어 ID는 필수입니다.")
    private Long ideaId;

    @Size(max = 200, message = "메모는 200자 이하여야 합니다.")
    private String memo;
}