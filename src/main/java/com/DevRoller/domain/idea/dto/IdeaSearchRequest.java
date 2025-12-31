package com.devroller.domain.idea.dto;

import com.devroller.domain.idea.entity.Idea;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 아이디어 검색 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdeaSearchRequest {

    private String keyword;
    private Long categoryId;
    private Idea.Difficulty difficulty;
    private Long tagId;
}