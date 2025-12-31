package com.devroller.domain.gamification.title.entity;

import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "titles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TitleType type;

    @Column
    private Integer requiredLevel;

    @Column(length = 50)
    private String requiredAchievementCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rarity rarity = Rarity.COMMON;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Builder
    public Title(String code, String name, String description, TitleType type,
                 Integer requiredLevel, String requiredAchievementCode,
                 Rarity rarity, Integer displayOrder) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.requiredLevel = requiredLevel;
        this.requiredAchievementCode = requiredAchievementCode;
        this.rarity = rarity != null ? rarity : Rarity.COMMON;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public enum TitleType { LEVEL, ACHIEVEMENT }
    public enum Rarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }
}
