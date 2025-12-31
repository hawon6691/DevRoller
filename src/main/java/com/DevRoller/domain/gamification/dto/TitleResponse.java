package com.devroller.domain.gamification.dto;

import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.entity.UserTitle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TitleResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String type;
    private Integer requiredLevel;
    private String requiredAchievementCode;
    private String rarity;
    
    // 사용자 보유 정보
    private Boolean isOwned;
    private Boolean isEquipped;
    private LocalDateTime acquiredAt;

    public static TitleResponse from(Title title, UserTitle userTitle) {
        TitleResponseBuilder builder = TitleResponse.builder()
                .id(title.getId())
                .code(title.getCode())
                .name(title.getName())
                .description(title.getDescription())
                .type(title.getType().name())
                .requiredLevel(title.getRequiredLevel())
                .requiredAchievementCode(title.getRequiredAchievementCode())
                .rarity(title.getRarity().name());

        if (userTitle != null) {
            builder.isOwned(true)
                    .isEquipped(userTitle.getIsEquipped())
                    .acquiredAt(userTitle.getAcquiredAt());
        } else {
            builder.isOwned(false)
                    .isEquipped(false);
        }

        return builder.build();
    }
}
