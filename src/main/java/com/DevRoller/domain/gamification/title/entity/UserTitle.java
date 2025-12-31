package com.devroller.domain.gamification.title.entity;

import com.devroller.domain.user.entity.User;
import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_titles",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "title_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTitle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", nullable = false)
    private Title title;

    @Column(nullable = false)
    private LocalDateTime acquiredAt;

    @Column(nullable = false)
    private Boolean isEquipped = false;

    @Builder
    public UserTitle(User user, Title title) {
        this.user = user;
        this.title = title;
        this.acquiredAt = LocalDateTime.now();
        this.isEquipped = false;
    }

    public void equip() { this.isEquipped = true; }
    public void unequip() { this.isEquipped = false; }
}
