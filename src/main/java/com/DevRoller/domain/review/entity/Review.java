package com.devroller.domain.review.entity;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "idea_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

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

    @Column(nullable = false)
    private Integer rating;  // 1-5

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer actualHours = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DifficultyFeedback difficultyFeedback;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Builder
    public Review(User user, Idea idea, Integer rating, String content,
                  Integer actualHours, DifficultyFeedback difficultyFeedback) {
        this.user = user;
        this.idea = idea;
        this.rating = rating;
        this.content = content;
        this.actualHours = actualHours != null ? actualHours : 0;
        this.difficultyFeedback = difficultyFeedback;
    }

    // 수정 메서드
    public void update(Integer rating, String content, Integer actualHours, 
                       DifficultyFeedback difficultyFeedback) {
        if (rating != null) this.rating = rating;
        if (content != null) this.content = content;
        if (actualHours != null) this.actualHours = actualHours;
        if (difficultyFeedback != null) this.difficultyFeedback = difficultyFeedback;
    }

    public void incrementLikeCount() { this.likeCount++; }
    public void decrementLikeCount() { if (this.likeCount > 0) this.likeCount--; }

    public enum DifficultyFeedback {
        EASIER_THAN_EXPECTED, AS_EXPECTED, HARDER_THAN_EXPECTED
    }
}
