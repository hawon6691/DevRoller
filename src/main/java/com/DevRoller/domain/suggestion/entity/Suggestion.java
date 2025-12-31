package com.devroller.domain.suggestion.entity;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suggestions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Suggestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggestion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 20)
    private String difficulty;

    @Column(length = 500)
    private String techStack;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDING;

    @Column(length = 500)
    private String adminComment;

    @Column(nullable = false)
    private Integer voteCount = 0;

    @Builder
    public Suggestion(User user, String title, String description,
                      Category category, String difficulty, String techStack) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.techStack = techStack;
    }

    public void approve(String adminComment) {
        this.status = Status.APPROVED;
        this.adminComment = adminComment;
    }

    public void reject(String adminComment) {
        this.status = Status.REJECTED;
        this.adminComment = adminComment;
    }

    public void incrementVoteCount() { this.voteCount++; }
    public void decrementVoteCount() { if (this.voteCount > 0) this.voteCount--; }

    public enum Status { PENDING, APPROVED, REJECTED }
}