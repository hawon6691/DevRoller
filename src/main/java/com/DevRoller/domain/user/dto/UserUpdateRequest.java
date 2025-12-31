package com.devroller.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 사용자 정보 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]+$", 
             message = "닉네임은 한글, 영문, 숫자, 밑줄만 사용 가능합니다.")
    private String nickname;

    private String profileImage;
}