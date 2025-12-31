package com.devroller.domain.gamification.service;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import com.devroller.domain.gamification.achievement.repository.AchievementRepository;
import com.devroller.domain.gamification.achievement.repository.UserAchievementRepository;
import com.devroller.domain.gamification.dto.*;
import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.entity.UserTitle;
import com.devroller.domain.gamification.title.repository.TitleRepository;
import com.devroller.domain.gamification.title.repository.UserTitleRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GamificationService {

    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;

    /**
     * 사용자 스테이터스 창 조회 (웹소설 스타일)
     */
    public StatusWindowResponse getStatusWindow(Long userId) {
        User user = findUserById(userId);
        
        // 장착된 칭호
        String equippedTitle = null;
        if (user.getEquippedTitleId() != null) {
            equippedTitle = titleRepository.findById(user.getEquippedTitleId())
                    .map(Title::getName)
                    .orElse(null);
        }

        // 업적 통계
        int completedAchievements = userAchievementRepository.countCompletedByUserId(userId);
        int totalAchievements = (int) achievementRepository.count();

        // 칭호 통계
        int ownedTitles = userTitleRepository.countByUserId(userId);
        int totalTitles = (int) titleRepository.count();

        return StatusWindowResponse.builder()
                .nickname(user.getNickname())
                .level(user.getLevel())
                .experience(user.getExperience())
                .experienceToNextLevel(user.getLevel() * 100)
                .equippedTitle(equippedTitle)
                .totalCompleted(user.getTotalCompleted())
                .currentStreak(user.getCurrentStreak())
                .maxStreak(user.getMaxStreak())
                .completedAchievements(completedAchievements)
                .totalAchievements(totalAchievements)
                .ownedTitles(ownedTitles)
                .totalTitles(totalTitles)
                .build();
    }

    /**
     * 전체 업적 목록 조회 (진행도 포함)
     */
    public List<AchievementResponse> getAllAchievements(Long userId) {
        List<Achievement> achievements = achievementRepository.findByIsHiddenFalseOrderByDisplayOrderAsc();
        
        return achievements.stream()
                .map(achievement -> {
                    UserAchievement userAchievement = userAchievementRepository
                            .findByUserIdAndAchievementId(userId, achievement.getId())
                            .orElse(null);
                    return AchievementResponse.from(achievement, userAchievement);
                })
                .collect(Collectors.toList());
    }

    /**
     * 달성한 업적 목록 조회
     */
    public List<AchievementResponse> getCompletedAchievements(Long userId) {
        List<UserAchievement> completedAchievements = 
                userAchievementRepository.findByUserIdAndIsCompletedTrue(userId);
        
        return completedAchievements.stream()
                .map(ua -> AchievementResponse.from(ua.getAchievement(), ua))
                .collect(Collectors.toList());
    }

    /**
     * 전체 칭호 목록 조회 (보유 여부 포함)
     */
    public List<TitleResponse> getAllTitles(Long userId) {
        List<Title> titles = titleRepository.findAllByOrderByDisplayOrderAsc();
        
        return titles.stream()
                .map(title -> {
                    UserTitle userTitle = userTitleRepository
                            .findByUserIdAndTitleId(userId, title.getId())
                            .orElse(null);
                    return TitleResponse.from(title, userTitle);
                })
                .collect(Collectors.toList());
    }

    /**
     * 보유한 칭호 목록 조회
     */
    public List<TitleResponse> getOwnedTitles(Long userId) {
        List<UserTitle> userTitles = userTitleRepository.findByUserId(userId);
        
        return userTitles.stream()
                .map(ut -> TitleResponse.from(ut.getTitle(), ut))
                .collect(Collectors.toList());
    }

    /**
     * 칭호 장착
     */
    @Transactional
    public void equipTitle(Long userId, Long titleId) {
        User user = findUserById(userId);

        // 칭호 보유 여부 확인
        if (!userTitleRepository.existsByUserIdAndTitleId(userId, titleId)) {
            throw new BusinessException(ErrorCode.TITLE_NOT_OWNED);
        }

        // 기존 장착 해제
        userTitleRepository.findByUserIdAndIsEquippedTrue(userId)
                .ifPresent(UserTitle::unequip);

        // 새 칭호 장착
        UserTitle userTitle = userTitleRepository.findByUserIdAndTitleId(userId, titleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TITLE_NOT_FOUND));
        userTitle.equip();

        user.equipTitle(titleId);
    }

    /**
     * 칭호 해제
     */
    @Transactional
    public void unequipTitle(Long userId) {
        User user = findUserById(userId);

        userTitleRepository.findByUserIdAndIsEquippedTrue(userId)
                .ifPresent(UserTitle::unequip);

        user.equipTitle(null);
    }

    // ===== Private Methods =====

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
