package com.devroller.domain.idea.service;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.service.CategoryService;
import com.devroller.domain.idea.dto.IdeaCreateRequest;
import com.devroller.domain.idea.dto.IdeaResponse;
import com.devroller.domain.idea.dto.IdeaSearchRequest;
import com.devroller.domain.idea.dto.IdeaUpdateRequest;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.IdeaTag;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.idea.repository.IdeaTagRepository;
import com.devroller.domain.tag.entity.Tag;
import com.devroller.domain.tag.repository.TagRepository;
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
 * 아이디어 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final IdeaTagRepository ideaTagRepository;
    private final TagRepository tagRepository;
    private final CategoryService categoryService;

    /**
     * 아이디어 조회 (ID)
     */
    public Idea findById(Long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));
    }

    /**
     * 아이디어 상세 조회
     */
    public IdeaResponse getIdea(Long ideaId) {
        Idea idea = findById(ideaId);
        List<Tag> tags = tagRepository.findByIdeaId(ideaId);
        return IdeaResponse.from(idea, tags);
    }

    /**
     * 아이디어 목록 조회 (페이징)
     */
    public Page<IdeaResponse> getIdeas(Pageable pageable) {
        return ideaRepository.findByIsActiveTrue(pageable)
                .map(idea -> {
                    List<Tag> tags = tagRepository.findByIdeaId(idea.getId());
                    return IdeaResponse.from(idea, tags);
                });
    }

    /**
     * 카테고리별 아이디어 조회
     */
    public Page<IdeaResponse> getIdeasByCategory(Long categoryId, Pageable pageable) {
        return ideaRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
                .map(idea -> {
                    List<Tag> tags = tagRepository.findByIdeaId(idea.getId());
                    return IdeaResponse.from(idea, tags);
                });
    }

    /**
     * 아이디어 검색
     */
    public Page<IdeaResponse> searchIdeas(IdeaSearchRequest request, Pageable pageable) {
        Page<Idea> ideas;

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            ideas = ideaRepository.findByTitleContainingIgnoreCaseAndIsActiveTrue(request.getKeyword(), pageable);
        } else if (request.getCategoryId() != null) {
            ideas = ideaRepository.findByCategoryIdAndIsActiveTrue(request.getCategoryId(), pageable);
        } else {
            ideas = ideaRepository.findByIsActiveTrue(pageable);
        }

        return ideas.map(idea -> {
            List<Tag> tags = tagRepository.findByIdeaId(idea.getId());
            return IdeaResponse.from(idea, tags);
        });
    }

    /**
     * 인기 아이디어 조회
     */
    public List<IdeaResponse> getPopularIdeas(int limit) {
        return ideaRepository.findPopularIdeas(PageRequest.of(0, limit))
                .stream()
                .map(idea -> {
                    List<Tag> tags = tagRepository.findByIdeaId(idea.getId());
                    return IdeaResponse.from(idea, tags);
                })
                .collect(Collectors.toList());
    }

    /**
     * 평점 높은 아이디어 조회
     */
    public List<IdeaResponse> getTopRatedIdeas(int limit) {
        return ideaRepository.findTopRatedIdeas(PageRequest.of(0, limit))
                .stream()
                .map(idea -> {
                    List<Tag> tags = tagRepository.findByIdeaId(idea.getId());
                    return IdeaResponse.from(idea, tags);
                })
                .collect(Collectors.toList());
    }

    /**
     * 아이디어 생성
     */
    @Transactional
    public IdeaResponse createIdea(IdeaCreateRequest request) {
        Category category = categoryService.findById(request.getCategoryId());

        Idea idea = Idea.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .difficulty(request.getDifficulty())
                .estimatedHours(request.getEstimatedHours())
                .techStack(request.getTechStack())
                .referenceUrl(request.getReferenceUrl())
                .build();

        Idea savedIdea = ideaRepository.save(idea);

        // 태그 연결
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
                IdeaTag ideaTag = IdeaTag.create(savedIdea, tag);
                ideaTagRepository.save(ideaTag);
            }
        }

        log.info("Idea created: {}", savedIdea.getId());
        List<Tag> tags = tagRepository.findByIdeaId(savedIdea.getId());
        return IdeaResponse.from(savedIdea, tags);
    }

    /**
     * 아이디어 수정
     */
    @Transactional
    public IdeaResponse updateIdea(Long ideaId, IdeaUpdateRequest request) {
        Idea idea = findById(ideaId);

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.findById(request.getCategoryId());
        }

        idea.update(
                request.getTitle(),
                request.getDescription(),
                category,
                request.getDifficulty(),
                request.getEstimatedHours(),
                request.getTechStack(),
                request.getReferenceUrl()
        );

        // 태그 업데이트
        if (request.getTagIds() != null) {
            ideaTagRepository.deleteByIdeaId(ideaId);
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
                IdeaTag ideaTag = IdeaTag.create(idea, tag);
                ideaTagRepository.save(ideaTag);
            }
        }

        log.info("Idea updated: {}", ideaId);
        List<Tag> tags = tagRepository.findByIdeaId(ideaId);
        return IdeaResponse.from(idea, tags);
    }

    /**
     * 아이디어 삭제 (비활성화)
     */
    @Transactional
    public void deleteIdea(Long ideaId) {
        Idea idea = findById(ideaId);
        idea.deactivate();
        log.info("Idea deactivated: {}", ideaId);
    }

    /**
     * 추첨 횟수 증가
     */
    @Transactional
    public void incrementPickCount(Long ideaId) {
        Idea idea = findById(ideaId);
        idea.incrementPickCount();
    }

    /**
     * 완료 횟수 증가
     */
    @Transactional
    public void incrementCompletedCount(Long ideaId) {
        Idea idea = findById(ideaId);
        idea.incrementCompletedCount();
    }

    /**
     * 평균 평점 업데이트
     */
    @Transactional
    public void updateAverageRating(Long ideaId, Double newAverage) {
        Idea idea = findById(ideaId);
        idea.updateAverageRating(newAverage);
    }
}