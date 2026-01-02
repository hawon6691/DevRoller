package com.devroller.domain.suggestion.service;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.repository.CategoryRepository;
import com.devroller.domain.suggestion.dto.SuggestionRequest;
import com.devroller.domain.suggestion.dto.SuggestionResponse;
import com.devroller.domain.suggestion.entity.Suggestion;
import com.devroller.domain.suggestion.repository.SuggestionRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 제안 생성
     */
    @Transactional
    public SuggestionResponse createSuggestion(Long userId, SuggestionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .difficulty(request.getDifficulty())
                .techStack(request.getTechStack())
                .build();

        suggestionRepository.save(suggestion);
        return SuggestionResponse.from(suggestion);
    }

    /**
     * 제안 수정 (본인만)
     */
    @Transactional
    public SuggestionResponse updateSuggestion(Long userId, Long suggestionId, SuggestionRequest request) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        // 작성자 확인
        if (!suggestion.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        // PENDING 상태에서만 수정 가능
        if (suggestion.getStatus() != Suggestion.Status.PENDING) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
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
     * 제안 삭제 (본인만)
     */
    @Transactional
    public void deleteSuggestion(Long userId, Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));

        // 작성자 확인
        if (!suggestion.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        suggestionRepository.delete(suggestion);
    }

    /**
     * 제안 투표 (추천)
     */
    @Transactional
    public void vote(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        suggestion.incrementVoteCount();
    }

    /**
     * 제안 투표 취소
     */
    @Transactional
    public void unvote(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        suggestion.decrementVoteCount();
    }

    /**
     * 제안 승인 (관리자)
     */
    @Transactional
    public SuggestionResponse approveSuggestion(Long suggestionId, String adminComment) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        suggestion.approve(adminComment);
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
        return SuggestionResponse.from(suggestion);
    }

    /**
     * 제안 단건 조회
     */
    public SuggestionResponse getSuggestion(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        return SuggestionResponse.from(suggestion);
    }

    /**
     * 대기 중인 제안 목록 (투표순)
     */
    public Page<SuggestionResponse> getPendingSuggestions(Pageable pageable) {
        return suggestionRepository.findByStatusOrderByVoteCountDesc(Suggestion.Status.PENDING, pageable)
                .map(SuggestionResponse::from);
    }

    /**
     * 상태별 제안 목록
     */
    public Page<SuggestionResponse> getSuggestionsByStatus(String status, Pageable pageable) {
        Suggestion.Status s = Suggestion.Status.valueOf(status.toUpperCase());
        return suggestionRepository.findByStatusOrderByCreatedAtDesc(s, pageable)
                .map(SuggestionResponse::from);
    }

    /**
     * 내 제안 목록
     */
    public Page<SuggestionResponse> getMySuggestions(Long userId, Pageable pageable) {
        return suggestionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(SuggestionResponse::from);
    }

    /**
     * 인기 제안 TOP 10
     */
    public List<SuggestionResponse> getTopSuggestions() {
        return suggestionRepository.findTop10ByStatusOrderByVoteCountDesc(Suggestion.Status.PENDING)
                .stream()
                .map(SuggestionResponse::from)
                .collect(Collectors.toList());
    }
}
