package com.devroller.domain.gamification.achievement.entity;

import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "achievements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 50)
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AchievementType type;

    @Column(nullable = false)
    private Integer requiredValue = 1;

    @Column(nullable = false)
    private Integer rewardExp = 0;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Builder
    public Achievement(String code, String name, String description, String icon,
                       AchievementType type, Integer requiredValue,
                       Integer rewardExp, Boolean isHidden, Integer displayOrder) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.type = type;
        this.requiredValue = requiredValue != null ? requiredValue : 1;
        this.rewardExp = rewardExp != null ? rewardExp : 0;
        this.isHidden = isHidden != null ? isHidden : false;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public enum AchievementType {
        COMPLETE_COUNT, STREAK, CATEGORY, DIFFICULTY, SPECIAL
    }
}
