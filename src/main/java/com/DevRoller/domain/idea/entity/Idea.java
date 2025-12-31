package com.devroller.domain.idea.entity;

import com.devroller.domain.category.entity.Category;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ideas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idea_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(nullable = false)
    private Integer estimatedHours = 0;

    @Column(length = 500)
    private String techStack;

    @Column(length = 500)
    private String referenceUrl;

    @Column(nullable = false)
    private Integer pickCount = 0;

    @Column(nullable = false)
    private Integer completedCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdeaTag> ideaTags = new ArrayList<>();

    @Builder
    public Idea(String title, String description, Category category, 
                Difficulty difficulty, Integer estimatedHours,
                String techStack, String referenceUrl) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty != null ? difficulty : Difficulty.MEDIUM;
        this.estimatedHours = estimatedHours != null ? estimatedHours : 0;
        this.techStack = techStack;
        this.referenceUrl = referenceUrl;
    }

    public void incrementPickCount() { this.pickCount++; }
    public void incrementCompletedCount() { this.completedCount++; }
    public void incrementLikeCount() { this.likeCount++; }
    public void decrementLikeCount() { if (this.likeCount > 0) this.likeCount--; }
    public void updateAverageRating(Double newAverage) { this.averageRating = newAverage; }
    public void addTag(IdeaTag ideaTag) { this.ideaTags.add(ideaTag); }
    public void removeTag(IdeaTag ideaTag) { this.ideaTags.remove(ideaTag); }
    public void activate() { this.isActive = true; }
    public void deactivate() { this.isActive = false; }

    public enum Difficulty { EASY, MEDIUM, HARD }

    public int getExperiencePoints() {
        return switch (this.difficulty) {
            case EASY -> 50;
            case MEDIUM -> 100;
            case HARD -> 200;
        };
    }
}