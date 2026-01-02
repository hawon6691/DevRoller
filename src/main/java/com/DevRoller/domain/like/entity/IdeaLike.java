package com.devroller.domain.like.entity;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "idea_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "idea_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @Builder
    public IdeaLike(User user, Idea idea) {
        this.user = user;
        this.idea = idea;
    }

    public static IdeaLike of(User user, Idea idea) {
        return IdeaLike.builder()
                .user(user)
                .idea(idea)
                .build();
    }
}
