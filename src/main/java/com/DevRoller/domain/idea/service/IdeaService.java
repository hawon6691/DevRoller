package com.devroller.domain.idea.service;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.repository.CategoryRepository;
import com.devroller.domain.gamification.event.ProjectCompletedEvent;
import com.devroller.domain.idea.dto.IdeaCreateRequest;
import com.devroller.domain.idea.dto.IdeaResponse;
import com.devroller.domain.idea.dto.IdeaUpdateRequest;
import com.devroller.domain.idea.dto.UserIdeaResponse;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.IdeaTag;
import com.devroller.domain.idea.entity.UserIdea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.idea.repository.UserIdeaRepository;
import com.devroller.domain.tag.entity.Tag;
import com.devroller.domain.tag.repository.TagRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserIdeaRepository userIdeaRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 아이디어 단건 조회
     */
    public IdeaResponse getIdeaById(Long ideaId) {
        Idea idea = findIdeaById(ideaId);
        return IdeaResponse.from(idea);
    }

    /**
     * 전체 아이디어 조회 (페이징)
     */
    public Page<IdeaResponse> getAllIdeas(Pageable pageable) {
        return ideaRepository.findByIsActiveTrue(pageable)
                .map(IdeaResponse::from);
    }

    /**
     * 카테고리별 아이디어 조회
     */
    public Page<IdeaResponse> getIdeasByCategory(Long categoryId, Pageable pageable) {
        return ideaRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
                .map(IdeaResponse::from);
    }

    /**
     * 난이도별 아이디어 조회
     */
    public Page<IdeaResponse> getIdeasByDifficulty(String difficulty, Pageable pageable) {
        Idea.Difficulty diff = Idea.Difficulty.valueOf(difficulty.toUpperCase());
        return ideaRepository.findByDifficultyAndIsActiveTrue(diff, pageable)
                .map(IdeaResponse::from);
    }

    /**
     * 인기 아이디어 조회
     */
    public Page<IdeaResponse> getPopularIdeas(Pageable pageable) {
        return ideaRepository.findPopularIdeas(pageable)
                .map(IdeaResponse::from);
    }

    /**
     * 평점 높은 아이디어 조회
     */
    public Page<IdeaResponse> getTopRatedIdeas(Pageable pageable) {
        return ideaRepository.findTopRatedIdeas(pageable)
                .map(IdeaResponse::from);
    }

    /**
     * 키워드 검색
     */
    public Page<IdeaResponse> searchIdeas(String keyword, Pageable pageable) {
        return ideaRepository.searchByKeyword(keyword, pageable)
                .map(IdeaResponse::from);
    }

    /**
     * 아이디어 생성 (관리자)
     */
    @Transactional
    public IdeaResponse createIdea(IdeaCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Idea idea = Idea.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .difficulty(Idea.Difficulty.valueOf(request.getDifficulty()))
                .estimatedHours(request.getEstimatedHours())
                .techStack(request.getTechStack())
                .referenceUrl(request.getReferenceUrl())
                .build();

        // 태그 추가
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
                IdeaTag.create(idea, tag);
            }
        }

        ideaRepository.save(idea);
        return IdeaResponse.from(idea);
    }

    /**
     * 아이디어 수정 (관리자)
     */
    @Transactional
    public IdeaResponse updateIdea(Long ideaId, IdeaUpdateRequest request) {
        Idea idea = findIdeaById(ideaId);

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        Idea.Difficulty difficulty = null;
        if (request.getDifficulty() != null) {
            difficulty = Idea.Difficulty.valueOf(request.getDifficulty());
        }

        idea.update(
                request.getTitle(),
                request.getDescription(),
                category,
                difficulty,
                request.getEstimatedHours(),
                request.getTechStack(),
                request.getReferenceUrl()
        );

        // 태그 업데이트
        if (request.getTagIds() != null) {
            idea.clearTags();
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
                IdeaTag.create(idea, tag);
            }
        }

        return IdeaResponse.from(idea);
    }

    /**
     * 아이디어 삭제 (비활성화)
     */
    @Transactional
    public void deleteIdea(Long ideaId) {
        Idea idea = findIdeaById(ideaId);
        idea.deactivate();
    }

    // ==================== UserIdea (프로젝트 진행) ====================

    /**
     * 프로젝트 시작
     */
    @Transactional
    public UserIdeaResponse startProject(Long userId, Long ideaId) {
        User user = findUserById(userId);
        Idea idea = findIdeaById(ideaId);

        // 이미 진행 중인지 확인
        if (userIdeaRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_IN_PROGRESS);
        }

        UserIdea userIdea = UserIdea.builder()
                .user(user)
                .idea(idea)
                .build();

        userIdeaRepository.save(userIdea);
        return UserIdeaResponse.from(userIdea);
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

        // 완료 처리
        userIdea.complete(githubUrl);
        
        // 아이디어 완료 카운트 증가
        Idea idea = userIdea.getIdea();
        idea.incrementCompletedCount();

        // 사용자 통계 업데이트
        User user = userIdea.getUser();
        user.completeProject();

        // 게이미피케이션 이벤트 발행
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

        userIdea.abandon();
        return UserIdeaResponse.from(userIdea);
    }

    /**
     * 내 프로젝트 목록 조회
     */
    public Page<UserIdeaResponse> getMyProjects(Long userId, Pageable pageable) {
        return userIdeaRepository.findByUserId(userId, pageable)
                .map(UserIdeaResponse::from);
    }

    /**
     * 상태별 프로젝트 조회
     */
    public Page<UserIdeaResponse> getMyProjectsByStatus(Long userId, String status, Pageable pageable) {
        UserIdea.Status s = UserIdea.Status.valueOf(status.toUpperCase());
        return userIdeaRepository.findByUserIdAndStatus(userId, s, pageable)
                .map(UserIdeaResponse::from);
    }

    /**
     * 진행률 업데이트
     */
    @Transactional
    public UserIdeaResponse updateProgress(Long userId, Long ideaId, Integer progress) {
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        userIdea.updateProgress(progress);
        return UserIdeaResponse.from(userIdea);
    }

    /**
     * GitHub URL 업데이트
     */
    @Transactional
    public UserIdeaResponse updateGithubUrl(Long userId, Long ideaId, String githubUrl) {
        UserIdea userIdea = userIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        userIdea.updateGithubUrl(githubUrl);
        return UserIdeaResponse.from(userIdea);
    }

    // ===== Private Methods =====

    private Idea findIdeaById(Long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
