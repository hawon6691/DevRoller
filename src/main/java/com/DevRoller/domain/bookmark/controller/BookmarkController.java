package com.devroller.domain.bookmark.controller;

import com.devroller.domain.bookmark.dto.BookmarkRequest;
import com.devroller.domain.bookmark.dto.BookmarkResponse;
import com.devroller.domain.bookmark.service.BookmarkService;
import com.devroller.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 북마크 API Controller
 */
@Tag(name = "Bookmark", description = "북마크 API")
@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 목록", description = "내 북마크 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<Page<BookmarkResponse>> getBookmarks(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<BookmarkResponse> response = bookmarkService.getBookmarks(userId, pageable);
        return ApiResponse.success(response);
    }

    @Operation(summary = "북마크 추가", description = "아이디어를 북마크합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookmarkResponse> addBookmark(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody BookmarkRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        BookmarkResponse response = bookmarkService.addBookmark(userId, request);
        return ApiResponse.success("북마크에 추가되었습니다.", response);
    }

    @Operation(summary = "북마크 삭제", description = "북마크를 삭제합니다.")
    @DeleteMapping("/{ideaId}")
    public ApiResponse<Void> removeBookmark(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        bookmarkService.removeBookmark(userId, ideaId);
        return ApiResponse.success("북마크가 삭제되었습니다.", null);
    }

    @Operation(summary = "북마크 메모 수정", description = "북마크 메모를 수정합니다.")
    @PatchMapping("/{ideaId}/memo")
    public ApiResponse<BookmarkResponse> updateMemo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(jwt.getSubject());
        BookmarkResponse response = bookmarkService.updateMemo(userId, ideaId, request.get("memo"));
        return ApiResponse.success("메모가 수정되었습니다.", response);
    }

    @Operation(summary = "북마크 여부 확인", description = "특정 아이디어의 북마크 여부를 확인합니다.")
    @GetMapping("/check/{ideaId}")
    public ApiResponse<Boolean> isBookmarked(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        boolean isBookmarked = bookmarkService.isBookmarked(userId, ideaId);
        return ApiResponse.success(isBookmarked);
    }

    @Operation(summary = "북마크 수", description = "내 북마크 총 개수를 조회합니다.")
    @GetMapping("/count")
    public ApiResponse<Long> getBookmarkCount(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        long count = bookmarkService.getBookmarkCount(userId);
        return ApiResponse.success(count);
    }
}