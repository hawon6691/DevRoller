package com.devroller.domain.pick.service;

import com.devroller.domain.idea.dto.IdeaResponse;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.pick.dto.PickRequest;
import com.devroller.domain.pick.dto.PickResponse;
import com.devroller.domain.pick.entity.PickHistory;
import com.devroller.domain.pick.repository.PickHistoryRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickService {

    private final PickHistoryRepository pickHistoryRepository;
    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    /**
     * 추첨 실행
     */
    @Transactional
    public PickResponse pick(Long userId, PickRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 후보 아이디어 조회
        List<Idea> candidates = getCandidates(request);
        
        if (candidates.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_AVAILABLE_IDEAS);
        }

        // 추첨 실행
        Idea selectedIdea = executeDrawing(candidates, request.getMethod());
        
        // Pick 카운트 증가
        selectedIdea.incrementPickCount();

        // 기록 저장
        PickHistory history = PickHistory.builder()
                .user(user)
                .idea(selectedIdea)
                .pickMethod(request.getMethod())
                .categoryFilter(request.getCategoryId() != null ? request.getCategoryId().toString() : null)
                .difficultyFilter(request.getDifficulty())
                .build();
        
        pickHistoryRepository.save(history);

        // 룰렛/사다리의 경우 후보 목록도 반환
        if (request.getMethod() == PickHistory.PickMethod.ROULETTE ||
            request.getMethod() == PickHistory.PickMethod.LADDER) {
            List<IdeaResponse> candidateResponses = candidates.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());
            return PickResponse.withCandidates(history, candidateResponses);
        }

        return PickResponse.from(history);
    }

    /**
     * 후보 아이디어 조회
     */
    private List<Idea> getCandidates(PickRequest request) {
        List<Idea> ideas;
        
        // 카테고리 + 난이도 필터
        if (request.getCategoryId() != null && request.getDifficulty() != null) {
            Idea.Difficulty difficulty = Idea.Difficulty.valueOf(request.getDifficulty());
            ideas = ideaRepository.findByCategoryIdAndDifficultyAndIsActiveTrue(
                    request.getCategoryId(), difficulty);
        }
        // 카테고리 필터만
        else if (request.getCategoryId() != null) {
            int count = request.getCount() != null ? request.getCount() : 10;
            ideas = ideaRepository.findRandomIdeasByCategory(request.getCategoryId(), count);
        }
        // 난이도 필터만
        else if (request.getDifficulty() != null) {
            Idea.Difficulty difficulty = Idea.Difficulty.valueOf(request.getDifficulty());
            ideas = ideaRepository.findByDifficultyAndIsActiveTrue(difficulty);
        }
        // 필터 없음
        else {
            ideas = ideaRepository.findByIsActiveTrue();
        }

        // 룰렛/사다리용 후보 수 제한
        if ((request.getMethod() == PickHistory.PickMethod.ROULETTE ||
             request.getMethod() == PickHistory.PickMethod.LADDER) && 
            request.getCount() != null && ideas.size() > request.getCount()) {
            Collections.shuffle(ideas);
            ideas = ideas.subList(0, request.getCount());
        }

        return ideas;
    }

    /**
     * 추첨 방식별 실행
     */
    private Idea executeDrawing(List<Idea> candidates, PickHistory.PickMethod method) {
        return switch (method) {
            case RANDOM -> randomPick(candidates);
            case ROULETTE -> roulettePick(candidates);
            case LADDER -> ladderPick(candidates);
            case LOTTERY -> lotteryPick(candidates);
        };
    }

    /**
     * 랜덤 추첨
     */
    private Idea randomPick(List<Idea> candidates) {
        int index = random.nextInt(candidates.size());
        return candidates.get(index);
    }

    /**
     * 룰렛 추첨 (가중치 적용 - 인기도 낮을수록 확률 높음)
     */
    private Idea roulettePick(List<Idea> candidates) {
        // 역가중치 계산: pickCount가 낮을수록 높은 가중치
        int maxPickCount = candidates.stream()
                .mapToInt(Idea::getPickCount)
                .max()
                .orElse(1) + 1;

        List<Integer> weights = candidates.stream()
                .map(idea -> maxPickCount - idea.getPickCount())
                .collect(Collectors.toList());

        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (int i = 0; i < candidates.size(); i++) {
            cumulativeWeight += weights.get(i);
            if (randomValue < cumulativeWeight) {
                return candidates.get(i);
            }
        }

        return candidates.get(candidates.size() - 1);
    }

    /**
     * 사다리 추첨 (완전 랜덤)
     */
    private Idea ladderPick(List<Idea> candidates) {
        return randomPick(candidates);
    }

    /**
     * 복권 추첨 (난이도별 가중치)
     */
    private Idea lotteryPick(List<Idea> candidates) {
        // 난이도별 가중치: HARD > MEDIUM > EASY
        List<Integer> weights = candidates.stream()
                .map(idea -> switch (idea.getDifficulty()) {
                    case EASY -> 1;
                    case MEDIUM -> 2;
                    case HARD -> 3;
                })
                .collect(Collectors.toList());

        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (int i = 0; i < candidates.size(); i++) {
            cumulativeWeight += weights.get(i);
            if (randomValue < cumulativeWeight) {
                return candidates.get(i);
            }
        }

        return candidates.get(candidates.size() - 1);
    }

    /**
     * 추첨 기록 조회
     */
    public Page<PickResponse> getPickHistory(Long userId, Pageable pageable) {
        return pickHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(PickResponse::from);
    }

    /**
     * 추첨 통계 조회
     */
    public PickStatsResponse getPickStats(Long userId) {
        long totalPicks = pickHistoryRepository.countByUserId(userId);
        List<Object[]> methodStats = pickHistoryRepository.countByPickMethod(userId);
        
        return PickStatsResponse.builder()
                .totalPicks(totalPicks)
                .methodStats(methodStats.stream()
                        .collect(Collectors.toMap(
                                arr -> ((PickHistory.PickMethod) arr[0]).name(),
                                arr -> ((Long) arr[1]).intValue()
                        )))
                .build();
    }

    @lombok.Builder
    @lombok.Getter
    public static class PickStatsResponse {
        private long totalPicks;
        private java.util.Map<String, Integer> methodStats;
    }
}
