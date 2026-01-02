package com.devroller.domain.bookmark.service;

import com.devroller.domain.bookmark.dto.BookmarkRequest;
import com.devroller.domain.bookmark.dto.BookmarkResponse;
import com.devroller.domain.bookmark.entity.Bookmark;
import com.devroller.domain.bookmark.repository.BookmarkRepository;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;

    /**
     * 북마크 추가
     */
    @Transactional
    public BookmarkResponse addBookmark(Long userId, Long ideaId, BookmarkRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));

        // 중복 체크
        if (bookmarkRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_BOOKMARKED);
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .idea(idea)
                .memo(request != null ? request.getMemo() : null)
                .build();

        bookmarkRepository.save(bookmark);
        
        // 아이디어 좋아요 카운트 증가
        idea.incrementLikeCount();

        return BookmarkResponse.from(bookmark);
    }

    /**
     * 북마크 삭제
     */
    @Transactional
    public void removeBookmark(Long userId, Long ideaId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));

        // 아이디어 좋아요 카운트 감소
        bookmark.getIdea().decrementLikeCount();

        bookmarkRepository.delete(bookmark);
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
     * 내 북마크 목록 조회
     */
    public Page<BookmarkResponse> getMyBookmarks(Long userId, Pageable pageable) {
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(BookmarkResponse::from);
    }

    /**
     * 북마크 여부 확인
     */
    public boolean isBookmarked(Long userId, Long ideaId) {
        return bookmarkRepository.existsByUserIdAndIdeaId(userId, ideaId);
    }

    /**
     * 북마크 수 조회
     */
    public int getBookmarkCount(Long userId) {
        return bookmarkRepository.countByUserId(userId);
    }
}
