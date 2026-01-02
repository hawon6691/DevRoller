package com.devroller.domain.bookmark.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookmarkRequest {

    @Size(max = 200, message = "메모는 200자를 초과할 수 없습니다")
    private String memo;
}
