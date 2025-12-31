package com.devroller.domain.pick.dto;

import com.devroller.domain.idea.dto.IdeaResponse;
import com.devroller.domain.pick.entity.PickHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PickResponse {

    private Long pickId;
    private String pickMethod;
    private IdeaResponse idea;
    private List<IdeaResponse> candidates;  // 룰렛/사다리용 후보 목록
    private LocalDateTime pickedAt;

    public static PickResponse from(PickHistory history) {
        return PickResponse.builder()
                .pickId(history.getId())
                .pickMethod(history.getPickMethod().name())
                .idea(IdeaResponse.from(history.getIdea()))
                .pickedAt(history.getCreatedAt())
                .build();
    }

    public static PickResponse withCandidates(PickHistory history, List<IdeaResponse> candidates) {
        return PickResponse.builder()
                .pickId(history.getId())
                .pickMethod(history.getPickMethod().name())
                .idea(IdeaResponse.from(history.getIdea()))
                .candidates(candidates)
                .pickedAt(history.getCreatedAt())
                .build();
    }
}
