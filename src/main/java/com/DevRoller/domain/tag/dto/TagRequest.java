package com.devroller.domain.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 태그 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagRequest {

    @NotBlank(message = "태그명은 필수입니다.")
    @Size(max = 50, message = "태그명은 50자 이하여야 합니다.")
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "색상은 HEX 형식이어야 합니다. (예: #FF5733)")
    private String color;
}