package com.devroller.domain.bookmark.service;

import com.devroller.domain.bookmark.dto.BookmarkRequest;
import com.devroller.domain.bookmark.dto.BookmarkResponse;
import com.devroller.domain.bookmark.entity.Bookmark;
import com.devroller.domain.bookmark.repository.BookmarkRepository;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.service.IdeaService;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 북마크 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final IdeaService ideaService;

    /**
     * 북마크 목록 조회
     */
    public List<BookmarkResponse> getBookmarks(Long userId) {
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(BookmarkResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 북마크 목록 조회 (페이징)
     */
    public Page<BookmarkResponse> getBookmarks(Long userId, Pageable pageable) {
        return bookmarkRepository.findByUserId(userId, pageable)
                .map(BookmarkResponse::from);
    }

    /**
     * 북마크 추가
     */
    @Transactional
    public BookmarkResponse addBookmark(Long userId, BookmarkRequest request) {
        // 이미 북마크한 경우
        if (bookmarkRepository.existsByUserIdAndIdeaId(userId, request.getIdeaId())) {
            throw new BusinessException(ErrorCode.ALREADY_BOOKMARKED);
        }

        User user = userService.findById(userId);
        Idea idea = ideaService.findById(request.getIdeaId());

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .idea(idea)
                .memo(request.getMemo())
                .build();

        // 아이디어 좋아요 수 증가
        idea.incrementLikeCount();

        Bookmark saved = bookmarkRepository.save(bookmark);
        log.info("User {} bookmarked idea {}", userId, request.getIdeaId());

        return BookmarkResponse.from(saved);
    }

    /**
     * 북마크 삭제
     */
    @Transactional
    public void removeBookmark(Long userId, Long ideaId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));

        // 아이디어 좋아요 수 감소
        bookmark.getIdea().decrementLikeCount();

        bookmarkRepository.delete(bookmark);
        log.info("User {} removed bookmark for idea {}", userId, ideaId);
    }

    /**
     * 북마크 메모 수정
     */
    @Transactional
    public BookmarkResponse updateMemo(Long userId, Long ideaId, String memo) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmark.updateMemo(memo);
        return BookmarkResponse.from(bookmark);
    }

    /**
     * 북마크 여부 확인
     */
    public boolean isBookmarked(Long userId, Long ideaId) {
        return bookmarkRepository.existsByUserIdAndIdeaId(userId, ideaId);
    }

    /**
     * 북마크 수
     */
    public long getBookmarkCount(Long userId) {
        return bookmarkRepository.countByUserId(userId);
    }
}