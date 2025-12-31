package com.devroller.domain.user.entity;

import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(length = 500)
    private String profileImage;

    @Column(nullable = false)
    private Integer level = 1;

    @Column(nullable = false)
    private Integer experience = 0;

    @Column(nullable = false)
    private Integer totalCompleted = 0;

    @Column(nullable = false)
    private Integer currentStreak = 0;

    @Column(nullable = false)
    private Integer maxStreak = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "equipped_title_id")
    private Long equippedTitleId;

    @Builder
    public User(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.level = 1;
        this.experience = 0;
        this.totalCompleted = 0;
        this.currentStreak = 0;
        this.maxStreak = 0;
        this.role = Role.USER;
        this.status = UserStatus.ACTIVE;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredExp = calculateRequiredExp(this.level);
        while (this.experience >= requiredExp && this.level < 50) {
            this.experience -= requiredExp;
            this.level++;
            requiredExp = calculateRequiredExp(this.level);
        }
    }

    private int calculateRequiredExp(int level) {
        return level * 100;
    }

    public void completeProject() {
        this.totalCompleted++;
        this.currentStreak++;
        if (this.currentStreak > this.maxStreak) {
            this.maxStreak = this.currentStreak;
        }
    }

    public void resetStreak() { this.currentStreak = 0; }
    public void equipTitle(Long titleId) { this.equippedTitleId = titleId; }
    public void updateProfile(String nickname, String profileImage) {
        if (nickname != null) this.nickname = nickname;
        if (profileImage != null) this.profileImage = profileImage;
    }
    public void changePassword(String newPassword) { this.password = newPassword; }
    public void deactivate() { this.status = UserStatus.INACTIVE; }

    public enum Role { USER, ADMIN }
    public enum UserStatus { ACTIVE, INACTIVE, BANNED }
}