package com.devroller.domain.gamification.event;

import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 업적 달성 시 발생하는 이벤트
 */
@Getter
public class AchievementUnlockedEvent extends ApplicationEvent {

    private final User user;
    private final Achievement achievement;

    public AchievementUnlockedEvent(Object source, User user, Achievement achievement) {
        super(source);
        this.user = user;
        this.achievement = achievement;
    }
}
