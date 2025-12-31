package com.devroller.domain.bookmark.entity;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmarks",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "idea_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @Column(length = 200)
    private String memo;

    @Builder
    public Bookmark(User user, Idea idea, String memo) {
        this.user = user;
        this.idea = idea;
        this.memo = memo;
    }

    public void updateMemo(String memo) { this.memo = memo; }
}