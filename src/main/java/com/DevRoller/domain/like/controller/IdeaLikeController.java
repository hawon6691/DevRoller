package com.devroller.domain.like.controller;

import com.devroller.domain.like.dto.IdeaLikeResponse;
import com.devroller.domain.like.service.IdeaLikeService;
import com.devroller.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class IdeaLikeController {

    private final IdeaLikeService ideaLikeService;

    /**
     * 좋아요 추가
     * POST /api/v1/likes/ideas/{ideaId}
     */
    @PostMapping("/ideas/{ideaId}")
    public ApiResponse<String> addLike(
            @PathVariable Long ideaId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ideaLikeService.addLike(userId, ideaId);
        return ApiResponse.success("좋아요를 추가했습니다.");
    }

    /**
     * 좋아요 취소
     * DELETE /api/v1/likes/ideas/{ideaId}
     */
    @DeleteMapping("/ideas/{ideaId}")
    public ApiResponse<String> removeLike(
            @PathVariable Long ideaId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ideaLikeService.removeLike(userId, ideaId);
        return ApiResponse.success("좋아요를 취소했습니다.");
    }

    /**
     * 내가 좋아요한 아이디어 목록
     * GET /api/v1/likes/my
     */
    @GetMapping("/my")
    public ApiResponse<List<IdeaLikeResponse>> getMyLikes(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<IdeaLikeResponse> likes = ideaLikeService.getMyLikes(userId);
        return ApiResponse.success(likes);
    }

    /**
     * 특정 아이디어 좋아요 여부 확인
     * GET /api/v1/likes/ideas/{ideaId}/check
     */
    @GetMapping("/ideas/{ideaId}/check")
    public ApiResponse<Boolean> checkLike(
            @PathVariable Long ideaId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isLiked = ideaLikeService.isLiked(userId, ideaId);
        return ApiResponse.success(isLiked);
    }

    /**
     * 특정 아이디어의 좋아요 수
     * GET /api/v1/likes/ideas/{ideaId}/count
     */
    @GetMapping("/ideas/{ideaId}/count")
    public ApiResponse<Long> getLikeCount(@PathVariable Long ideaId) {
        long count = ideaLikeService.getLikeCount(ideaId);
        return ApiResponse.success(count);
    }
}
