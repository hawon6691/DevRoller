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
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Column
    private LocalDateTime achievedAt;

    @Column(nullable = false)
    private Integer currentProgress = 0;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @Builder
    public UserAchievement(User user, Achievement achievement) {
        this.user = user;
        this.achievement = achievement;
        this.currentProgress = 0;
        this.isCompleted = false;
    }

    // 진행도 1 증가
    public void incrementProgress() {
        this.currentProgress++;
        checkCompletion();
    }

    // 진행도 직접 설정
    public void updateProgress(int newProgress) {
        this.currentProgress = newProgress;
        checkCompletion();
    }

    // 완료 체크
    private void checkCompletion() {
        if (!this.isCompleted && this.currentProgress >= this.achievement.getRequiredValue()) {
            this.isCompleted = true;
            this.achievedAt = LocalDateTime.now();
        }
    }

    // 즉시 완료 처리
    public void complete() {
        this.currentProgress = this.achievement.getRequiredValue();
        this.isCompleted = true;
        this.achievedAt = LocalDateTime.now();
    }

    // 진행률 퍼센트
    public int getProgressPercent() {
        if (this.achievement.getRequiredValue() == 0) return 100;
        return Math.min(100, (this.currentProgress * 100) / this.achievement.getRequiredValue());
    }
}
