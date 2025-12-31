package com.devroller.domain.gamification.event;

import com.devroller.domain.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 레벨업 시 발생하는 이벤트
 */
@Getter
public class LevelUpEvent extends ApplicationEvent {

    private final User user;
    private final int previousLevel;
    private final int newLevel;

    public LevelUpEvent(Object source, User user, int previousLevel, int newLevel) {
        super(source);
        this.user = user;
        this.previousLevel = previousLevel;
        this.newLevel = newLevel;
    }
}
