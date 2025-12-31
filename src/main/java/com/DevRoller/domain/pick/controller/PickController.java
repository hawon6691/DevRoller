package com.devroller.domain.pick.controller;

import com.devroller.domain.pick.dto.PickHistoryResponse;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 추첨 API Controller
 */
@Tag(name = "Pick", description = "추첨 API")
@RestController
@RequestMapping("/api/pick")
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    @Operation(summary = "추첨하기", description = "조건에 맞는 아이디어 중 하나를 추첨합니다.")
    @PostMapping
    public ApiResponse<PickResponse> pick(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PickRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        PickResponse response = pickService.pick(userId, request);
        return ApiResponse.success("추첨 완료! 새로운 프로젝트가 선택되었습니다.", response);
    }

    @Operation(summary = "추첨 후 프로젝트 시작", description = "추첨된 아이디어로 프로젝트를 시작합니다.")
    @PostMapping("/{ideaId}/start")
    public ApiResponse<Void> startProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        pickService.startProject(userId, ideaId);
        return ApiResponse.success("프로젝트가 시작되었습니다.", null);
    }

    @Operation(summary = "추첨 기록", description = "내 추첨 기록을 조회합니다.")
    @GetMapping("/history")
    public ApiResponse<Page<PickHistoryResponse>> getPickHistory(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<PickHistoryResponse> response = pickService.getPickHistory(userId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "추첨 통계", description = "내 추첨 방식별 통계를 조회합니다.")
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getPickStats(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        List<Object[]> methodStats = pickService.getPickStatistics(userId);
        long totalCount = pickService.getTotalPickCount(userId);
        
        return ApiResponse.success(Map.of(
                "totalCount", totalCount,
                "byMethod", methodStats
        ));
    }
}