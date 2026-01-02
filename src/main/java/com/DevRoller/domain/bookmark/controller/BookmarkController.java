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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bookmark", description = "북마크 API")
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 추가", description = "아이디어를 북마크에 추가합니다")
    @PostMapping("/{ideaId}")
    public ResponseEntity<ApiResponse<BookmarkResponse>> addBookmark(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @Valid @RequestBody(required = false) BookmarkRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        BookmarkResponse response = bookmarkService.addBookmark(userId, ideaId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "북마크 삭제", description = "북마크를 삭제합니다")
    @DeleteMapping("/{ideaId}")
    public ResponseEntity<ApiResponse<Void>> removeBookmark(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        bookmarkService.removeBookmark(userId, ideaId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "북마크 메모 수정", description = "북마크의 메모를 수정합니다")
    @PatchMapping("/{ideaId}/memo")
    public ResponseEntity<ApiResponse<BookmarkResponse>> updateMemo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId,
            @RequestParam String memo) {
        Long userId = Long.parseLong(jwt.getSubject());
        BookmarkResponse response = bookmarkService.updateMemo(userId, ideaId, memo);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 북마크 목록", description = "내 북마크 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookmarkResponse>>> getMyBookmarks(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        Long userId = Long.parseLong(jwt.getSubject());
        Page<BookmarkResponse> bookmarks = bookmarkService.getMyBookmarks(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(bookmarks));
    }

    @Operation(summary = "북마크 여부 확인", description = "해당 아이디어가 북마크 되어있는지 확인합니다")
    @GetMapping("/{ideaId}/check")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long ideaId) {
        Long userId = Long.parseLong(jwt.getSubject());
        boolean isBookmarked = bookmarkService.isBookmarked(userId, ideaId);
        return ResponseEntity.ok(ApiResponse.success(isBookmarked));
    }
}
