package com.devroller.domain.pick.entity;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pick_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickHistory extends BaseEntity {

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
    private PickMethod pickMethod;

    @Column(length = 50)
    private String categoryFilter;

    @Column(length = 20)
    private String difficultyFilter;

    @Builder
    public PickHistory(User user, Idea idea, PickMethod pickMethod,
                       String categoryFilter, String difficultyFilter) {
        this.user = user;
        this.idea = idea;
        this.pickMethod = pickMethod;
        this.categoryFilter = categoryFilter;
        this.difficultyFilter = difficultyFilter;
    }

    public enum PickMethod { RANDOM, ROULETTE, LADDER, LOTTERY }
}
