package com.devroller.domain.user.controller;

import com.devroller.domain.user.dto.LoginRequest;
import com.devroller.domain.user.dto.LoginResponse;
import com.devroller.domain.user.dto.SignUpRequest;
import com.devroller.domain.user.dto.SignUpResponse;
import com.devroller.domain.user.service.AuthService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 API Controller
 */
@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ApiResponse.success("회원가입이 완료되었습니다.", response);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success("로그인 성공", response);
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        LoginResponse response = authService.refreshToken(userId);
        return ApiResponse.success("토큰이 갱신되었습니다.", response);
    }

    @Operation(summary = "이메일 중복 확인", description = "이메일 사용 가능 여부를 확인합니다.")
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = authService.checkEmailDuplicate(email);
        String message = isDuplicate ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.";
        return ApiResponse.success(message, !isDuplicate);
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 사용 가능 여부를 확인합니다.")
    @GetMapping("/check-nickname")
    public ApiResponse<Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = authService.checkNicknameDuplicate(nickname);
        String message = isDuplicate ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.";
        return ApiResponse.success(message, !isDuplicate);
    }
}