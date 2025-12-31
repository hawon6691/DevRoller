package com.devroller.domain.gamification.title.dto;

import com.devroller.domain.gamification.title.entity.Title;
import lombok.Builder;
import lombok.Getter;

/**
 * 칭호 응답 DTO
 */
@Getter
@Builder
public class TitleResponse {

    private Long titleId;
    private String code;
    private String name;
    private String description;
    private String type;
    private Integer requiredLevel;
    private String requiredAchievementCode;
    private String rarity;

    public static TitleResponse from(Title title) {
        return TitleResponse.builder()
                .titleId(title.getId())
                .code(title.getCode())
                .name(title.getName())
                .description(title.getDescription())
                .type(title.getType().name())
                .requiredLevel(title.getRequiredLevel())
                .requiredAchievementCode(title.getRequiredAchievementCode())
                .rarity(title.getRarity().name())
                .build();
    }
}