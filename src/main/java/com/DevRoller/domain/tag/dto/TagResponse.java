package com.devroller.domain.tag.dto;

import com.devroller.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;

/**
 * 태그 응답 DTO
 */
@Getter
@Builder
public class TagResponse {

    private Long tagId;
    private String name;
    private String color;

    public static TagResponse from(Tag tag) {
        return TagResponse.builder()
                .tagId(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}