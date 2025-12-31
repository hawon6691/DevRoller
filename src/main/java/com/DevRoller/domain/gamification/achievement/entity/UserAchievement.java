package com.devroller.domain.gamification.achievement.entity;

import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_achievements",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "achievement_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAchievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_achievement_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Column(nullable = false)
    private LocalDateTime achievedAt;

    @Column(nullable = false)
    private Integer currentProgress = 0;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @Builder
    public UserAchievement(User user, Achievement achievement) {
        this.user = user;
        this.achievement = achievement;
    }

    public void incrementProgress() {
        this.currentProgress++;
        if (!this.isCompleted && this.currentProgress >= this.achievement.getRequiredValue()) {
            this.isCompleted = true;
            this.achievedAt = LocalDateTime.now();
        }
    }

    public void complete() {
        this.isCompleted = true;
        this.achievedAt = LocalDateTime.now();
    }

    public int getProgressPercent() {
        if (this.achievement.getRequiredValue() == 0) return 100;
        return Math.min(100, (this.currentProgress * 100) / this.achievement.getRequiredValue());
    }
}