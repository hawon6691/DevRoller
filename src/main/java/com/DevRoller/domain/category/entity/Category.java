package com.devroller.domain.category.entity;

import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(length = 50)
    private String icon;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Builder
    public Category(String name, String description, String icon, Integer displayOrder) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.isActive = true;
    }

    public void update(String name, String description, String icon, Integer displayOrder) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (icon != null) this.icon = icon;
        if (displayOrder != null) this.displayOrder = displayOrder;
    }

    public void activate() { this.isActive = true; }
    public void deactivate() { this.isActive = false; }
}