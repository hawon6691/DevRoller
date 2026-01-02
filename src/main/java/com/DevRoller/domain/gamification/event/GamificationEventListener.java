package com.devroller.domain.gamification.event;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import com.devroller.domain.gamification.achievement.repository.AchievementRepository;
import com.devroller.domain.gamification.achievement.repository.UserAchievementRepository;
import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.entity.UserTitle;
import com.devroller.domain.gamification.title.repository.TitleRepository;
import com.devroller.domain.gamification.title.repository.UserTitleRepository;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 게이미피케이션 이벤트 리스너
 * 프로젝트 완료, 레벨업 등의 이벤트를 수신하여 업적/칭호를 자동 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GamificationEventListener {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 프로젝트 완료 이벤트 처리
     */
    @EventListener
    @Transactional
    public void handleProjectCompleted(ProjectCompletedEvent event) {
        User user = event.getUser();
        Idea idea = event.getIdea();
        
        log.info("Processing project completion for user: {}, idea: {}", 
                user.getId(), idea.getId());

        // 1. 완료 횟수 기반 업적 체크
        checkCompletionAchievements(user);

        // 2. 난이도 기반 업적 체크
        checkDifficultyAchievements(user, idea);

        // 3. 카테고리 기반 업적 체크
        checkCategoryAchievements(user, idea);

        // 4. 스트릭 기반 업적 체크
        checkStreakAchievements(user);

        // 5. 경험치 부여 및 레벨업 체크
        int previousLevel = user.getLevel();
        user.addExperience(idea.getExperiencePoints());
        userRepository.save(user);
        
        if (user.getLevel() > previousLevel) {
            eventPublisher.publishEvent(
                new LevelUpEvent(this, user, previousLevel, user.getLevel())
            );
        }
    }

    /**
     * 레벨업 이벤트 처리
     */
    @EventListener
    @Transactional
    public void handleLevelUp(LevelUpEvent event) {
        User user = event.getUser();
        int newLevel = event.getNewLevel();
        
        log.info("Processing level up for user: {}, new level: {}", 
                user.getId(), newLevel);

        // 레벨 기반 칭호 확인 및 부여
        List<Title> levelTitles = titleRepository.findByTypeAndRequiredLevelLessThanEqual(
            Title.TitleType.LEVEL, newLevel
        );

        for (Title title : levelTitles) {
            if (!userTitleRepository.existsByUserIdAndTitleId(user.getId(), title.getId())) {
                UserTitle userTitle = UserTitle.builder()
                    .user(user)
                    .title(title)
                    .build();
                userTitleRepository.save(userTitle);
                log.info("Granted title '{}' to user {}", title.getName(), user.getId());
            }
        }
    }

    /**
     * 업적 달성 이벤트 처리
     */
    @EventListener
    @Transactional
    public void handleAchievementUnlocked(AchievementUnlockedEvent event) {
        User user = event.getUser();
        Achievement achievement = event.getAchievement();
        
        log.info("Processing achievement unlock for user: {}, achievement: {}", 
                user.getId(), achievement.getCode());

        // 업적 연동 칭호 확인 및 부여
        Optional<Title> linkedTitle = titleRepository.findByRequiredAchievementCode(achievement.getCode());
        
        linkedTitle.ifPresent(title -> {
            if (!userTitleRepository.existsByUserIdAndTitleId(user.getId(), title.getId())) {
                UserTitle userTitle = UserTitle.builder()
                    .user(user)
                    .title(title)
                    .build();
                userTitleRepository.save(userTitle);
                log.info("Granted title '{}' to user {} for achievement '{}'", 
                        title.getName(), user.getId(), achievement.getName());
            }
        });

        // 업적 보상 경험치 부여
        if (achievement.getRewardExp() > 0) {
            user.addExperience(achievement.getRewardExp());
            userRepository.save(user);
        }
    }

    /**
     * 완료 횟수 기반 업적 체크
     */
    private void checkCompletionAchievements(User user) {
        List<Achievement> completionAchievements = achievementRepository
            .findByType(Achievement.AchievementType.COMPLETE_COUNT);

        for (Achievement achievement : completionAchievements) {
            processAchievement(user, achievement, user.getTotalCompleted());
        }
    }

    /**
     * 난이도 기반 업적 체크
     */
    private void checkDifficultyAchievements(User user, Idea idea) {
        List<Achievement> difficultyAchievements = achievementRepository
            .findByType(Achievement.AchievementType.DIFFICULTY);

        for (Achievement achievement : difficultyAchievements) {
            // 업적 코드에 난이도가 포함되어 있는지 확인
            String code = achievement.getCode().toUpperCase();
            if (code.contains(idea.getDifficulty().name())) {
                UserAchievement userAchievement = getOrCreateUserAchievement(user, achievement);
                if (!userAchievement.getIsCompleted()) {
                    userAchievement.incrementProgress();
                    userAchievementRepository.save(userAchievement);
                    
                    if (userAchievement.getIsCompleted()) {
                        publishAchievementUnlocked(user, achievement);
                    }
                }
            }
        }
    }

    /**
     * 카테고리 기반 업적 체크
     */
    private void checkCategoryAchievements(User user, Idea idea) {
        List<Achievement> categoryAchievements = achievementRepository
            .findByType(Achievement.AchievementType.CATEGORY);

        String categoryName = idea.getCategory().getName().toUpperCase().replace(" ", "_");
        
        for (Achievement achievement : categoryAchievements) {
            String code = achievement.getCode().toUpperCase();
            if (code.contains(categoryName)) {
                UserAchievement userAchievement = getOrCreateUserAchievement(user, achievement);
                if (!userAchievement.getIsCompleted()) {
                    userAchievement.incrementProgress();
                    userAchievementRepository.save(userAchievement);
                    
                    if (userAchievement.getIsCompleted()) {
                        publishAchievementUnlocked(user, achievement);
                    }
                }
            }
        }
    }

    /**
     * 스트릭 기반 업적 체크
     */
    private void checkStreakAchievements(User user) {
        List<Achievement> streakAchievements = achievementRepository
            .findByType(Achievement.AchievementType.STREAK);

        for (Achievement achievement : streakAchievements) {
            processAchievement(user, achievement, user.getCurrentStreak());
        }
    }

    /**
     * 업적 진행도 처리 공통 로직
     */
    private void processAchievement(User user, Achievement achievement, int currentValue) {
        UserAchievement userAchievement = getOrCreateUserAchievement(user, achievement);
        
        if (!userAchievement.getIsCompleted()) {
            userAchievement.updateProgress(currentValue);
            userAchievementRepository.save(userAchievement);
            
            if (userAchievement.getIsCompleted()) {
                publishAchievementUnlocked(user, achievement);
            }
        }
    }

    /**
     * UserAchievement 조회 또는 생성
     */
    private UserAchievement getOrCreateUserAchievement(User user, Achievement achievement) {
        return userAchievementRepository
            .findByUserIdAndAchievementId(user.getId(), achievement.getId())
            .orElseGet(() -> {
                UserAchievement newUserAchievement = UserAchievement.builder()
                    .user(user)
                    .achievement(achievement)
                    .build();
                return userAchievementRepository.save(newUserAchievement);
            });
    }

    /**
     * 업적 달성 이벤트 발행
     */
    private void publishAchievementUnlocked(User user, Achievement achievement) {
        log.info("Achievement unlocked: {} for user {}", achievement.getName(), user.getId());
        eventPublisher.publishEvent(new AchievementUnlockedEvent(this, user, achievement));
    }
}
