package com.devroller.domain.idea.service;

import com.devroller.domain.gamification.event.ProjectCompletedEvent;
import com.devroller.domain.idea.dto.UserIdeaResponse;
import com.devroller.domain.idea.dto.UserIdeaUpdateRequest;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.UserIdea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.idea.repository.UserIdeaRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 프로젝트 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserIdeaService {

    private final UserIdeaRepository userIdeaRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 사용자의 진행중인 프로젝트 목록
     */
    public List<UserIdeaResponse> getInProgressProjects(Long userId) {
        return userIdeaRepository.findByUserIdAndStatusOrderByStartedAtDesc(userId, UserIdea.Status.IN_PROGRESS)
                .stream()
                .map(UserIdeaResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 완료된 프로젝트 목록 (페이징)
     */
    public Page<UserIdeaResponse> getCompletedProjects(Long userId, Pageable pageable) {
        return userIdeaRepository.findByUserIdAndStatus(userId, UserIdea.Status.COMPLETED, pageable)
                .map(UserIdeaResponse::from);
    }

    /**
     * 프로젝트 상세 조회
     */
    public UserIdeaResponse getProject(Long userId, Long ideaId) {
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        return UserIdeaResponse.from(userIdea);
    }

    /**
     * 프로젝트 시작
     */
    @Transactional
    public UserIdeaResponse startProject(Long userId, Long ideaId) {
        // 이미 존재하는지 확인
        if (userIdeaRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_IN_PROGRESS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));

        UserIdea userIdea = UserIdea.builder()
                .user(user)
                .idea(idea)
                .build();

        UserIdea saved = userIdeaRepository.save(userIdea);
        log.info("User {} started project {}", userId, ideaId);

        return UserIdeaResponse.from(saved);
    }

    /**
     * 프로젝트 완료
     */
    @Transactional
    public UserIdeaResponse completeProject(Long userId, Long ideaId, String githubUrl) {
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (userIdea.getStatus() == UserIdea.Status.COMPLETED) {
            throw new BusinessException(ErrorCode.ALREADY_COMPLETED);
        }

        userIdea.complete(githubUrl);

        // 아이디어 완료 횟수 증가
        Idea idea = userIdea.getIdea();
        idea.incrementCompletedCount();

        // 사용자 완료 처리 (스트릭, 총 완료수)
        User user = userIdea.getUser();
        user.completeProject();

        log.info("User {} completed project {}", userId, ideaId);

        // 프로젝트 완료 이벤트 발행 (업적/칭호/경험치 자동 처리)
        eventPublisher.publishEvent(new ProjectCompletedEvent(this, user, idea));

        return UserIdeaResponse.from(userIdea);
    }

    /**
     * 프로젝트 포기
     */
    @Transactional
    public UserIdeaResponse abandonProject(Long userId, Long ideaId) {
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (userIdea.getStatus() != UserIdea.Status.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "진행중인 프로젝트만 포기할 수 있습니다.");
        }

        userIdea.abandon();
        log.info("User {} abandoned project {}", userId, ideaId);

        return UserIdeaResponse.from(userIdea);
    }

    /**
     * 진행률 업데이트
     */
    @Transactional
    public UserIdeaResponse updateProgress(Long userId, Long ideaId, UserIdeaUpdateRequest request) {
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (request.getProgressPercent() != null) {
            userIdea.updateProgress(request.getProgressPercent());
        }
        if (request.getMemo() != null) {
            userIdea.updateMemo(request.getMemo());
        }
        if (request.getGithubUrl() != null) {
            userIdea.updateGithubUrl(request.getGithubUrl());
        }

        return UserIdeaResponse.from(userIdea);
    }

    /**
     * 사용자의 프로젝트 통계
     */
    public ProjectStats getProjectStats(Long userId) {
        long inProgress = userIdeaRepository.countByUserIdAndStatus(userId, UserIdea.Status.IN_PROGRESS);
        long completed = userIdeaRepository.countByUserIdAndStatus(userId, UserIdea.Status.COMPLETED);
        long abandoned = userIdeaRepository.countByUserIdAndStatus(userId, UserIdea.Status.ABANDONED);

        return new ProjectStats(inProgress, completed, abandoned);
    }

    /**
     * 프로젝트 통계 record
     */
    public record ProjectStats(long inProgress, long completed, long abandoned) {}
}