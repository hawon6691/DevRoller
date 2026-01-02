package com.devroller.domain.hidden.dto;

import com.devroller.domain.hidden.entity.HiddenIdea;
import com.devroller.domain.idea.dto.IdeaResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HiddenIdeaResponse {

    private Long id;
    private Long userId;
    private IdeaResponse idea;
    private String reason;
    private LocalDateTime hiddenAt;

    public static HiddenIdeaResponse from(HiddenIdea hiddenIdea) {
        return HiddenIdeaResponse.builder()
                .id(hiddenIdea.getId())
                .userId(hiddenIdea.getUser().getId())
                .idea(IdeaResponse.from(hiddenIdea.getIdea()))
                .reason(hiddenIdea.getReason())
                .hiddenAt(hiddenIdea.getCreatedAt())
                .build();
    }
}
