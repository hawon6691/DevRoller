package com.devroller.domain.pick.service;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.UserIdea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.idea.repository.UserIdeaRepository;
import com.devroller.domain.idea.service.IdeaService;
import com.devroller.domain.pick.dto.PickRequest;
import com.devroller.domain.pick.dto.PickResponse;
import com.devroller.domain.pick.dto.PickHistoryResponse;
import com.devroller.domain.pick.entity.PickHistory;
import com.devroller.domain.pick.repository.PickHistoryRepository;
import com.devroller.domain.pick.strategy.*;
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
 * 추첨 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickService {

    private final IdeaRepository ideaRepository;
    private final UserIdeaRepository userIdeaRepository;
    private final PickHistoryRepository pickHistoryRepository;
    private final UserService userService;
    private final IdeaService ideaService;

    private final RandomPickStrategy randomPickStrategy;
    private final RoulettePickStrategy roulettePickStrategy;
    private final LadderPickStrategy ladderPickStrategy;
    private final LotteryPickStrategy lotteryPickStrategy;

    /**
     * 추첨 실행
     */
    @Transactional
    public PickResponse pick(Long userId, PickRequest request) {
        User user = userService.findById(userId);

        // 추첨 가능한 아이디어 목록 조회
        List<Idea> availableIdeas = getAvailableIdeas(userId, request);

        if (availableIdeas.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_AVAILABLE_IDEAS);
        }

        // 추첨 전략 선택
        PickStrategy strategy = getStrategy(request.getPickMethod());

        // 추첨 실행
        Idea pickedIdea = strategy.pick(availableIdeas);

        if (pickedIdea == null) {
            throw new BusinessException(ErrorCode.PICK_FAILED);
        }

        // 추첨 횟수 증가
        pickedIdea.incrementPickCount();

        // 추첨 기록 저장
        PickHistory history = PickHistory.builder()
                .user(user)
                .idea(pickedIdea)
                .pickMethod(request.getPickMethod())
                .categoryFilter(request.getCategoryId() != null ? request.getCategoryId().toString() : null)
                .difficultyFilter(request.getDifficulty() != null ? request.getDifficulty().name() : null)
                .build();

        pickHistoryRepository.save(history);
        log.info("User {} picked idea {} using {}", userId, pickedIdea.getId(), request.getPickMethod());

        return PickResponse.from(pickedIdea, history.getId());
    }

    /**
     * 추첨 후 프로젝트 시작
     */
    @Transactional
    public void startProject(Long userId, Long ideaId) {
        User user = userService.findById(userId);
        Idea idea = ideaService.findById(ideaId);

        // 이미 진행중인지 확인
        if (userIdeaRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_IN_PROGRESS);
        }

        UserIdea userIdea = UserIdea.builder()
                .user(user)
                .idea(idea)
                .build();

        userIdeaRepository.save(userIdea);
        log.info("User {} started project {}", userId, ideaId);
    }

    /**
     * 추첨 기록 조회
     */
    public Page<PickHistoryResponse> getPickHistory(Long userId, Pageable pageable) {
        return pickHistoryRepository.findByUserId(userId, pageable)
                .map(PickHistoryResponse::from);
    }

    /**
     * 추첨 통계 조회
     */
    public List<Object[]> getPickStatistics(Long userId) {
        return pickHistoryRepository.countByUserIdGroupByPickMethod(userId);
    }

    /**
     * 사용자의 총 추첨 횟수
     */
    public long getTotalPickCount(Long userId) {
        return pickHistoryRepository.countByUserId(userId);
    }

    /**
     * 추첨 가능한 아이디어 목록 조회
     */
    private List<Idea> getAvailableIdeas(Long userId, PickRequest request) {
        Long categoryId = request.getCategoryId();
        Idea.Difficulty difficulty = request.getDifficulty();

        if (categoryId != null && difficulty != null) {
            return ideaRepository.findAvailableIdeasForUserByCategoryAndDifficulty(userId, categoryId, difficulty);
        } else if (categoryId != null) {
            return ideaRepository.findAvailableIdeasForUserByCategory(userId, categoryId);
        } else if (difficulty != null) {
            return ideaRepository.findAvailableIdeasForUserByDifficulty(userId, difficulty);
        } else {
            return ideaRepository.findAvailableIdeasForUser(userId);
        }
    }

    /**
     * 추첨 전략 선택
     */
    private PickStrategy getStrategy(PickHistory.PickMethod method) {
        return switch (method) {
            case RANDOM -> randomPickStrategy;
            case ROULETTE -> roulettePickStrategy;
            case LADDER -> ladderPickStrategy;
            case LOTTERY -> lotteryPickStrategy;
        };
    }
}