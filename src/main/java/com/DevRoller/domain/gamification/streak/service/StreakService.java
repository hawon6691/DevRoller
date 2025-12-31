package com.devroller.domain.gamification.streak.service;

import com.devroller.domain.gamification.streak.dto.StreakResponse;
import com.devroller.domain.gamification.streak.entity.Streak;
import com.devroller.domain.gamification.streak.repository.StreakRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 스트릭 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StreakService {

    private final StreakRepository streakRepository;
    private final UserService userService;

    /**
     * 활동 기록
     */
    @Transactional
    public void recordActivity(Long userId, Streak.ActivityType activityType) {
        LocalDate today = LocalDate.now();

        streakRepository.findByUserIdAndActivityDateAndActivityType(userId, today, activityType)
                .ifPresentOrElse(
                        Streak::incrementCount,
                        () -> {
                            User user = userService.findById(userId);
                            Streak streak = Streak.builder()
                                    .user(user)
                                    .activityDate(today)
                                    .activityType(activityType)
                                    .build();
                            streakRepository.save(streak);
                        }
                );

        log.debug("User {} recorded activity: {}", userId, activityType);
    }

    /**
     * 특정 기간의 스트릭 조회
     */
    public List<StreakResponse> getStreaks(Long userId, LocalDate startDate, LocalDate endDate) {
        return streakRepository.findByUserIdAndActivityDateBetween(userId, startDate, endDate)
                .stream()
                .map(StreakResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 최근 N일 스트릭 조회
     */
    public List<StreakResponse> getRecentStreaks(Long userId, int days) {
        return streakRepository.findRecentStreaks(userId, PageRequest.of(0, days))
                .stream()
                .map(StreakResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 현재 연속 스트릭 계산
     */
    public int calculateCurrentStreak(Long userId) {
        LocalDate today = LocalDate.now();
        List<LocalDate> activityDates = streakRepository.findActivityDatesByUserIdOrderByDateDesc(userId, today);

        if (activityDates.isEmpty()) {
            return 0;
        }

        // 오늘 또는 어제 활동이 없으면 스트릭 0
        LocalDate firstDate = activityDates.get(0);
        if (!firstDate.equals(today) && !firstDate.equals(today.minusDays(1))) {
            return 0;
        }

        int streak = 1;
        LocalDate expectedDate = firstDate.minusDays(1);

        for (int i = 1; i < activityDates.size(); i++) {
            if (activityDates.get(i).equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return streak;
    }

    /**
     * 총 활동 일수
     */
    public long getTotalActivityDays(Long userId) {
        return streakRepository.countDistinctActivityDaysByUserId(userId);
    }

    /**
     * 활동 타입별 총 횟수
     */
    public long getTotalActivityCount(Long userId, Streak.ActivityType type) {
        Long count = streakRepository.sumCountByUserIdAndActivityType(userId, type);
        return count != null ? count : 0;
    }

    /**
     * 오늘 활동 여부
     */
    public boolean hasActivityToday(Long userId) {
        return streakRepository.existsByUserIdAndActivityDate(userId, LocalDate.now());
    }

    /**
     * 스트릭 캘린더 데이터 (이번 달)
     */
    public List<StreakResponse> getMonthlyCalendar(Long userId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return getStreaks(userId, startDate, endDate);
    }
}