package com.devroller.domain.gamification.achievement.service;

import com.devroller.domain.gamification.achievement.dto.AchievementResponse;
import com.devroller.domain.gamification.achievement.dto.UserAchievementResponse;
import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import com.devroller.domain.gamification.achievement.repository.AchievementRepository;
import com.devroller.domain.gamification.achievement.repository.UserAchievementRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 업적 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserService userService;

    /**
     * 전체 업적 목록 (히든 제외)
     */
    public List<AchievementResponse> getAllAchievements() {
        return achievementRepository.findByIsHiddenFalseOrderByDisplayOrderAsc()
                .stream()
                .map(AchievementResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 타입별 업적 목록
     */
    public List<AchievementResponse> getAchievementsByType(Achievement.AchievementType type) {
        return achievementRepository.findByTypeOrderByDisplayOrderAsc(type)
                .stream()
                .map(AchievementResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 업적 현황
     */
    public List<UserAchievementResponse> getUserAchievements(Long userId) {
        return userAchievementRepository.findByUserId(userId)
                .stream()
                .map(UserAchievementResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 완료된 업적만
     */
    public List<UserAchievementResponse> getCompletedAchievements(Long userId) {
        return userAchievementRepository.findByUserIdAndIsCompletedTrue(userId)
                .stream()
                .map(UserAchievementResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 최근 달성한 업적
     */
    public List<UserAchievementResponse> getRecentAchievements(Long userId, int limit) {
        return userAchievementRepository.findRecentAchievements(userId, PageRequest.of(0, limit))
                .stream()
                .map(UserAchievementResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 업적 진행도 업데이트
     */
    @Transactional
    public void updateProgress(Long userId, String achievementCode, int progress) {
        Achievement achievement = achievementRepository.findByCode(achievementCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACHIEVEMENT_NOT_FOUND));

        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> createUserAchievement(userId, achievement));

        if (!userAchievement.getIsCompleted()) {
            int beforeProgress = userAchievement.getCurrentProgress();
            userAchievement.updateProgress(progress);

            if (userAchievement.getIsCompleted() && beforeProgress < achievement.getRequiredValue()) {
                // 업적 달성! 보상 경험치 지급
                if (achievement.getRewardExp() > 0) {
                    userService.addExperience(userId, achievement.getRewardExp());
                }
                log.info("User {} achieved: {} (+{}XP)", userId, achievementCode, achievement.getRewardExp());
            }
        }
    }

    /**
     * 업적 진행도 증가
     */
    @Transactional
    public void incrementProgress(Long userId, String achievementCode) {
        Achievement achievement = achievementRepository.findByCode(achievementCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACHIEVEMENT_NOT_FOUND));

        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> createUserAchievement(userId, achievement));

        if (!userAchievement.getIsCompleted()) {
            userAchievement.incrementProgress();

            if (userAchievement.getIsCompleted()) {
                if (achievement.getRewardExp() > 0) {
                    userService.addExperience(userId, achievement.getRewardExp());
                }
                log.info("User {} achieved: {} (+{}XP)", userId, achievementCode, achievement.getRewardExp());
            }
        }
    }

    /**
     * 특별 업적 즉시 달성
     */
    @Transactional
    public void completeSpecialAchievement(Long userId, String achievementCode) {
        Achievement achievement = achievementRepository.findByCode(achievementCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACHIEVEMENT_NOT_FOUND));

        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> createUserAchievement(userId, achievement));

        if (!userAchievement.getIsCompleted()) {
            userAchievement.complete();

            if (achievement.getRewardExp() > 0) {
                userService.addExperience(userId, achievement.getRewardExp());
            }
            log.info("User {} achieved special: {} (+{}XP)", userId, achievementCode, achievement.getRewardExp());
        }
    }

    /**
     * UserAchievement 생성
     */
    private UserAchievement createUserAchievement(Long userId, Achievement achievement) {
        User user = userService.findById(userId);
        UserAchievement userAchievement = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .build();
        return userAchievementRepository.save(userAchievement);
    }

    /**
     * 사용자의 완료된 업적 수
     */
    public long getCompletedCount(Long userId) {
        return userAchievementRepository.countByUserIdAndIsCompletedTrue(userId);
    }
}