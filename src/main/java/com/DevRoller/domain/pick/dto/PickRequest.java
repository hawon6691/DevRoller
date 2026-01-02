package com.devroller.domain.pick.dto;

import com.devroller.domain.pick.entity.PickHistory;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PickRequest {

    @NotNull(message = "추첨 방식을 선택해주세요")
    private PickHistory.PickMethod method;

    private Long categoryId;

    private String difficulty;  // EASY, MEDIUM, HARD

    private Integer count;  // 후보 수 (사다리, 룰렛용)

    private List<Long> tagIds;  // 태그 필터 (여러 태그 AND 조건)

    private Boolean excludeCompleted;  // 완료한 주제 제외 (기본값: false)

    private Boolean excludeInProgress;  // 진행 중인 주제 제외 (기본값: false)
}
