package com.devroller.domain.gamification.streak.scheduler;

import com.devroller.domain.gamification.streak.entity.Streak;
import com.devroller.domain.gamification.streak.repository.StreakRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 스트릭 관리 스케줄러
 * 매일 자정에 비활성 사용자의 스트릭을 리셋
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StreakScheduler {

    private final UserRepository userRepository;
    private final StreakRepository streakRepository;

    /**
     * 매일 자정 (00:00)에 실행
     * 어제 활동이 없는 사용자의 스트릭을 리셋
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetInactiveUserStreaks() {
        log.info("Starting daily streak reset job...");
        
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        // 현재 스트릭이 1 이상인 모든 사용자 조회
        List<User> usersWithStreak = userRepository.findByCurrentStreakGreaterThan(0);
        
        int resetCount = 0;
        
        for (User user : usersWithStreak) {
            // 어제 COMPLETE 활동이 있었는지 확인
            boolean hadActivityYesterday = streakRepository
                .existsByUserIdAndActivityDateAndActivityType(
                    user.getId(), 
                    yesterday, 
                    Streak.ActivityType.COMPLETE
                );
            
            if (!hadActivityYesterday) {
                // 스트릭 리셋
                user.resetStreak();
                userRepository.save(user);
                resetCount++;
                
                log.debug("Reset streak for user: {}", user.getId());
            }
        }
        
        log.info("Daily streak reset completed. Reset {} user(s)", resetCount);
    }

    /**
     * 매주 월요일 새벽 3시에 실행
     * 30일 이상 된 스트릭 기록 정리 (선택적)
     */
    @Scheduled(cron = "0 0 3 * * MON")
    @Transactional
    public void cleanupOldStreakRecords() {
        log.info("Starting weekly streak cleanup job...");
        
        LocalDate cutoffDate = LocalDate.now().minusDays(30);
        
        int deletedCount = streakRepository.deleteByActivityDateBefore(cutoffDate);
        
        log.info("Weekly streak cleanup completed. Deleted {} old record(s)", deletedCount);
    }

    /**
     * 수동 스트릭 동기화 (관리자용)
     * 사용자의 실제 연속 완료일 기반으로 스트릭 재계산
     */
    @Transactional
    public void syncUserStreak(Long userId) {
        log.info("Syncing streak for user: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        // 최근 활동일부터 역순으로 연속일 계산
        LocalDate checkDate = LocalDate.now();
        int streak = 0;
        
        while (true) {
            boolean hadActivity = streakRepository
                .existsByUserIdAndActivityDateAndActivityType(
                    userId, 
                    checkDate, 
                    Streak.ActivityType.COMPLETE
                );
            
            if (hadActivity) {
                streak++;
                checkDate = checkDate.minusDays(1);
            } else if (checkDate.equals(LocalDate.now())) {
                // 오늘 활동이 없어도 어제까지 확인
                checkDate = checkDate.minusDays(1);
            } else {
                break;
            }
        }
        
        user.updateStreak(streak);
        userRepository.save(user);
        
        log.info("Synced streak for user {}: {}", userId, streak);
    }

    /**
     * 전체 사용자 스트릭 동기화 (관리자용)
     */
    @Transactional
    public void syncAllUserStreaks() {
        log.info("Starting full streak sync for all users...");
        
        List<User> allUsers = userRepository.findAll();
        
        for (User user : allUsers) {
            try {
                syncUserStreak(user.getId());
            } catch (Exception e) {
                log.error("Failed to sync streak for user: {}", user.getId(), e);
            }
        }
        
        log.info("Full streak sync completed for {} users", allUsers.size());
    }
}
