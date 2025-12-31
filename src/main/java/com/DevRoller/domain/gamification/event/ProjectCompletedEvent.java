package com.devroller.domain.gamification.event;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 프로젝트 완료 시 발생하는 이벤트
 */
@Getter
public class ProjectCompletedEvent extends ApplicationEvent {

    private final User user;
    private final Idea idea;

    public ProjectCompletedEvent(Object source, User user, Idea idea) {
        super(source);
        this.user = user;
        this.idea = idea;
    }
}
