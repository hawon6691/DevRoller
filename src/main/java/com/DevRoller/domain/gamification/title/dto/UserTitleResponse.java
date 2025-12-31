package com.devroller.domain.gamification.title.dto;

import com.devroller.domain.gamification.title.entity.UserTitle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 칭호 응답 DTO
 */
@Getter
@Builder
public class UserTitleResponse {

    private Long userTitleId;
    private Long titleId;
    private String code;
    private String name;
    private String description;
    private String rarity;
    private Boolean isEquipped;
    private LocalDateTime acquiredAt;

    public static UserTitleResponse from(UserTitle ut) {
        return UserTitleResponse.builder()
                .userTitleId(ut.getId())
                .titleId(ut.getTitle().getId())
                .code(ut.getTitle().getCode())
                .name(ut.getTitle().getName())
                .description(ut.getTitle().getDescription())
                .rarity(ut.getTitle().getRarity().name())
                .isEquipped(ut.getIsEquipped())
                .acquiredAt(ut.getAcquiredAt())
                .build();
    }
}