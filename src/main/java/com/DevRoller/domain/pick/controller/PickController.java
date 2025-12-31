package com.devroller.domain.pick.controller;

import com.devroller.domain.pick.dto.PickRequest;
import com.devroller.domain.pick.dto.PickResponse;
import com.devroller.domain.pick.service.PickService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pick", description = "추첨 API")
@RestController
@RequestMapping("/api/v1/picks")
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    @Operation(summary = "추첨 실행", description = "설정된 방식으로 프로젝트 주제를 추첨합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<PickResponse>> pick(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PickRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        PickResponse response = pickService.pick(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "추첨 기록 조회", description = "내 추첨 기록을 조회합니다")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<PickResponse>>> getPickHistory(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<PickResponse> history = pickService.getPickHistory(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @Operation(summary = "추첨 통계", description = "내 추첨 통계를 조회합니다")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<PickService.PickStatsResponse>> getPickStats(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        PickService.PickStatsResponse stats = pickService.getPickStats(userId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
