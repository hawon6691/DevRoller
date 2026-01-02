package com.devroller.domain.hidden.entity;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hidden_ideas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "idea_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HiddenIdea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @Column(length = 500)
    private String reason;  // 숨김 이유 (선택사항)

    @Builder
    public HiddenIdea(User user, Idea idea, String reason) {
        this.user = user;
        this.idea = idea;
        this.reason = reason;
    }

    public static HiddenIdea of(User user, Idea idea, String reason) {
        return HiddenIdea.builder()
                .user(user)
                .idea(idea)
                .reason(reason)
                .build();
    }
}
