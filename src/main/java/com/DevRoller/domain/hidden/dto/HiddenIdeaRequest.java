package com.devroller.domain.hidden.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HiddenIdeaRequest {
    private String reason;  // 숨김 이유 (선택사항)
}
