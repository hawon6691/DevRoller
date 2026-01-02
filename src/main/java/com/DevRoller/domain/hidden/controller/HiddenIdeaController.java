package com.devroller.domain.hidden.controller;

import com.devroller.domain.hidden.dto.HiddenIdeaRequest;
import com.devroller.domain.hidden.dto.HiddenIdeaResponse;
import com.devroller.domain.hidden.service.HiddenIdeaService;
import com.devroller.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hidden")
@RequiredArgsConstructor
public class HiddenIdeaController {

    private final HiddenIdeaService hiddenIdeaService;

    /**
     * 아이디어 숨기기
     * POST /api/v1/hidden/ideas/{ideaId}
     */
    @PostMapping("/ideas/{ideaId}")
    public ApiResponse<String> hideIdea(
            @PathVariable Long ideaId,
            @RequestBody(required = false) HiddenIdeaRequest request,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        String reason = request != null ? request.getReason() : null;
        hiddenIdeaService.hideIdea(userId, ideaId, reason);
        return ApiResponse.success("아이디어를 숨겼습니다.");
    }

    /**
     * 아이디어 숨김 해제
     * DELETE /api/v1/hidden/ideas/{ideaId}
     */
    @DeleteMapping("/ideas/{ideaId}")
    public ApiResponse<String> unhideIdea(
            @PathVariable Long ideaId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        hiddenIdeaService.unhideIdea(userId, ideaId);
        return ApiResponse.success("아이디어 숨김을 해제했습니다.");
    }

    /**
     * 내가 숨긴 아이디어 목록
     * GET /api/v1/hidden/my
     */
    @GetMapping("/my")
    public ApiResponse<List<HiddenIdeaResponse>> getMyHiddenIdeas(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<HiddenIdeaResponse> hiddenIdeas = hiddenIdeaService.getMyHiddenIdeas(userId);
        return ApiResponse.success(hiddenIdeas);
    }

    /**
     * 특정 아이디어 숨김 여부 확인
     * GET /api/v1/hidden/ideas/{ideaId}/check
     */
    @GetMapping("/ideas/{ideaId}/check")
    public ApiResponse<Boolean> checkHidden(
            @PathVariable Long ideaId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isHidden = hiddenIdeaService.isHidden(userId, ideaId);
        return ApiResponse.success(isHidden);
    }
}
