package com.devroller.domain.idea.entity;

import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_ideas", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "idea_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserIdea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.IN_PROGRESS;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Column(length = 500)
    private String githubUrl;

    @Column
    private Integer progressPercent = 0;

    @Column(length = 500)
    private String memo;

    @Builder
    public UserIdea(User user, Idea idea) {
        this.user = user;
        this.idea = idea;
        this.status = Status.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
        this.progressPercent = 0;
    }

    public void complete(String githubUrl) {
        this.status = Status.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.githubUrl = githubUrl;
        this.progressPercent = 100;
    }

    public void abandon() { 
        this.status = Status.ABANDONED; 
    }
    
    public void updateProgress(Integer percent) { 
        if (percent != null && percent >= 0 && percent <= 100) {
            this.progressPercent = percent;
        }
    }
    
    public void updateMemo(String memo) { 
        this.memo = memo; 
    }
    
    public void updateGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public enum Status { IN_PROGRESS, COMPLETED, ABANDONED }
}
