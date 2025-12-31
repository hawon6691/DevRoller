package com.devroller.domain.gamification.streak.entity;

import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "streaks",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_date", "activity_type"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Streak extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityType activityType;

    @Column(nullable = false)
    private Integer count = 1;

    @Builder
    public Streak(User user, LocalDate activityDate, ActivityType activityType) {
        this.user = user;
        this.activityDate = activityDate;
        this.activityType = activityType;
        this.count = 1;
    }

    public void incrementCount() { this.count++; }

    public enum ActivityType { PICK, COMPLETE, REVIEW }
}
