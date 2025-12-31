package com.devroller.domain.pick.dto;

import com.devroller.domain.pick.entity.PickHistory;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickRequest {

    @NotNull(message = "추첨 방식을 선택해주세요")
    private PickHistory.PickMethod method;

    private Long categoryId;
    
    private String difficulty;  // EASY, MEDIUM, HARD
    
    private Integer count;  // 후보 수 (사다리, 룰렛용)
}
