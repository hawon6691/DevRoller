package com.devroller.domain.suggestion.service;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.service.CategoryService;
import com.devroller.domain.suggestion.dto.SuggestionRequest;
import com.devroller.domain.suggestion.dto.SuggestionResponse;
import com.devroller.domain.suggestion.entity.Suggestion;
import com.devroller.domain.suggestion.repository.SuggestionRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주제 제안 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    /**
     * 대기중인 제안 목록 (투표순)
     */
    public Page<SuggestionResponse> getPendingSuggestions(Pageable pageable) {
        return suggestionRepository.findPendingOrderByVoteCountDesc(pageable)
                .map(SuggestionResponse::from);
    }

    /**
     * 상태별 제안 목록
     */
    public Page<SuggestionResponse> getSuggestionsByStatus(Suggestion.Status status, Pageable pageable) {
        return suggestionRepository.findByStatus(status, pageable)
                .map(SuggestionResponse::from);
    }

    /**
     * 사용자의 제안 목록
     */
    public Page<SuggestionResponse> getUserSuggestions(Long userId, Pageable pageable) {
        return suggestionRepository.findByUserId(userId, pageable)
                .map(SuggestionResponse::from);
    }

    /**
     * 제안 상세 조회
     */
    public SuggestionResponse getSuggestion(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        return SuggestionResponse.from(suggestion);
    }

    /**
     * 제안 작성
     */
    @Transactional
    public SuggestionResponse createSuggestion(Long userId, SuggestionRequest request) {
        User user = userService.findById(userId);

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.findById(request.getCategoryId());
        }

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .difficulty(request.getDifficulty())
                .techStack(request.getTechStack())
                .build();

        Suggestion saved = suggestionRepository.save(suggestion);
        log.info("User {} created suggestion: {}", userId, saved.getId());

        return SuggestionResponse.from(saved);
    }

    /**
     * 제안 수정
     */
    @Transactional
    public SuggestionResponse updateSuggestion(Long userId, Long suggestionId, SuggestionRequest request) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        if (!suggestion.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 제안만 수정할 수 있습니다.");
        }

        if (suggestion.getStatus() != Suggestion.Status.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "대기중인 제안만 수정할 수 있습니다.");
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.findById(request.getCategoryId());
        }

        suggestion.update(
                request.getTitle(),
                request.getDescription(),
                category,
                request.getDifficulty(),
                request.getTechStack()
        );

        return SuggestionResponse.from(suggestion);
    }

    /**
     * 제안 삭제
     */
    @Transactional
    public void deleteSuggestion(Long userId, Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        if (!suggestion.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "본인의 제안만 삭제할 수 있습니다.");
        }

        suggestionRepository.delete(suggestion);
        log.info("User {} deleted suggestion {}", userId, suggestionId);
    }

    /**
     * 제안 투표
     */
    @Transactional
    public void voteSuggestion(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        if (suggestion.getStatus() != Suggestion.Status.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "대기중인 제안에만 투표할 수 있습니다.");
        }

        suggestion.incrementVoteCount();
    }

    /**
     * 제안 승인 (관리자)
     */
    @Transactional
    public SuggestionResponse approveSuggestion(Long suggestionId, String adminComment) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        suggestion.approve(adminComment);
        log.info("Suggestion {} approved", suggestionId);

        return SuggestionResponse.from(suggestion);
    }

    /**
     * 제안 거절 (관리자)
     */
    @Transactional
    public SuggestionResponse rejectSuggestion(Long suggestionId, String adminComment) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        suggestion.reject(adminComment);
        log.info("Suggestion {} rejected", suggestionId);

        return SuggestionResponse.from(suggestion);
    }

    /**
     * 인기 제안 (투표수 높은)
     */
    public List<SuggestionResponse> getTopVotedSuggestions(int limit) {
        return suggestionRepository.findTopVotedPending(PageRequest.of(0, limit))
                .stream()
                .map(SuggestionResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 최근 승인된 제안
     */
    public List<SuggestionResponse> getRecentApproved(int limit) {
        return suggestionRepository.findRecentApproved(PageRequest.of(0, limit))
                .stream()
                .map(SuggestionResponse::from)
                .collect(Collectors.toList());
    }
}