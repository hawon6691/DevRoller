package com.devroller.domain.stats.service;

import com.devroller.domain.gamification.title.repository.TitleRepository;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.entity.UserIdea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.idea.repository.UserIdeaRepository;
import com.devroller.domain.stats.dto.PopularIdeasResponse;
import com.devroller.domain.stats.dto.UserRankingResponse;
import com.devroller.domain.stats.dto.UserStatsResponse;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final UserIdeaRepository userIdeaRepository;
    private final TitleRepository titleRepository;

    /**
     * 인기 주제 TOP N 조회
     */
    public PopularIdeasResponse getPopularIdeas(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Idea> byPickCount = ideaRepository.findPopularIdeas(pageable).getContent();
        List<Idea> byCompleteCount = ideaRepository.findAll(pageable).getContent()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getCompletedCount(), a.getCompletedCount()))
                .limit(limit)
                .collect(Collectors.toList());
        List<Idea> byRating = ideaRepository.findTopRatedIdeas(pageable).getContent();

        return PopularIdeasResponse.of(byPickCount, byCompleteCount, byRating);
    }

    /**
     * 사용자 랭킹 조회
     */
    public UserRankingResponse getUserRanking(String type, int limit, Long currentUserId) {
        Pageable pageable = PageRequest.of(0, limit);
        List<User> topUsers;

        switch (type.toUpperCase()) {
            case "LEVEL":
                topUsers = userRepository.findTopByLevel(pageable);
                break;
            case "COMPLETED":
                topUsers = userRepository.findTopByCompleted(pageable);
                break;
            case "STREAK":
                topUsers = userRepository.findByStatusOrderByCurrentStreakDesc(User.UserStatus.ACTIVE, pageable).getContent();
                break;
            default:
                topUsers = userRepository.findTopByLevel(pageable);
        }

        // 랭킹 리스트 생성
        List<UserRankingResponse.RankingUser> rankings = topUsers.stream()
                .map(user -> {
                    int rank = topUsers.indexOf(user) + 1;
                    String titleName = user.getEquippedTitleId() != null
                            ? titleRepository.findById(user.getEquippedTitleId())
                                    .map(title -> title.getName())
                                    .orElse(null)
                            : null;
                    return UserRankingResponse.RankingUser.fromUser(user, rank, titleName);
                })
                .collect(Collectors.toList());

        // 내 랭킹 계산
        UserRankingResponse.MyRank myRank = null;
        if (currentUserId != null) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            List<User> allUsers = getAllUsersForRanking(type);
            int myRankPosition = findUserRank(allUsers, currentUser, type);
            myRank = UserRankingResponse.MyRank.of(myRankPosition, allUsers.size());
        }

        return UserRankingResponse.builder()
                .rankings(rankings)
                .myRank(myRank)
                .build();
    }

    /**
     * 개인 통계 조회
     */
    public UserStatsResponse getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 프로젝트 통계
        List<UserIdea> allProjects = userIdeaRepository.findByUserId(userId, Pageable.unpaged()).getContent();
        long completedCount = allProjects.stream().filter(ui -> ui.getStatus() == UserIdea.Status.COMPLETED).count();
        long inProgressCount = allProjects.stream().filter(ui -> ui.getStatus() == UserIdea.Status.IN_PROGRESS).count();
        double completionRate = allProjects.isEmpty() ? 0.0 : (double) completedCount / allProjects.size() * 100;

        UserStatsResponse.ProjectStats projectStats = UserStatsResponse.ProjectStats.builder()
                .total(allProjects.size())
                .completed((int) completedCount)
                .inProgress((int) inProgressCount)
                .completionRate(Math.round(completionRate * 100.0) / 100.0)
                .build();

        // 카테고리별 통계
        Map<String, Long> categoryCountMap = new HashMap<>();
        allProjects.stream()
                .filter(ui -> ui.getStatus() == UserIdea.Status.COMPLETED)
                .forEach(ui -> {
                    String categoryName = ui.getIdea().getCategory().getName();
                    categoryCountMap.put(categoryName, categoryCountMap.getOrDefault(categoryName, 0L) + 1);
                });

        List<UserStatsResponse.CategoryStat> categoryStats = categoryCountMap.entrySet().stream()
                .map(entry -> {
                    double percentage = completedCount > 0 ? (double) entry.getValue() / completedCount * 100 : 0.0;
                    return UserStatsResponse.CategoryStat.builder()
                            .categoryName(entry.getKey())
                            .count(entry.getValue().intValue())
                            .percentage(Math.round(percentage * 100.0) / 100.0)
                            .build();
                })
                .sorted((a, b) -> Integer.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());

        // 스트릭 정보
        UserStatsResponse.StreakInfo streakInfo = UserStatsResponse.StreakInfo.builder()
                .current(user.getCurrentStreak())
                .max(user.getMaxStreak())
                .lastActivityDate(user.getUpdatedAt() != null
                        ? user.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE)
                        : null)
                .build();

        // 레벨 정보
        int requiredExp = user.getLevel() * 100;
        double progress = requiredExp > 0 ? (double) user.getExperience() / requiredExp * 100 : 0.0;

        UserStatsResponse.LevelInfo levelInfo = UserStatsResponse.LevelInfo.builder()
                .currentLevel(user.getLevel())
                .currentExp(user.getExperience())
                .requiredExp(requiredExp)
                .progress(Math.round(progress * 100.0) / 100.0)
                .build();

        return UserStatsResponse.of(projectStats, categoryStats, streakInfo, levelInfo);
    }

    // Helper methods
    private List<User> getAllUsersForRanking(String type) {
        switch (type.toUpperCase()) {
            case "LEVEL":
                return userRepository.findTopByLevel(Pageable.unpaged());
            case "COMPLETED":
                return userRepository.findTopByCompleted(Pageable.unpaged());
            case "STREAK":
                return userRepository.findByStatusOrderByCurrentStreakDesc(User.UserStatus.ACTIVE, Pageable.unpaged()).getContent();
            default:
                return userRepository.findTopByLevel(Pageable.unpaged());
        }
    }

    private int findUserRank(List<User> users, User targetUser, String type) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(targetUser.getId())) {
                return i + 1;
            }
        }
        return users.size(); // 못 찾으면 마지막 순위
    }
}
