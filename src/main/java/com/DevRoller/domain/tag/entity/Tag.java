package com.devroller.domain.tag.entity;

import com.devroller.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 7)
    private String color;  // HEX 색상 코드

    @Builder
    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(String name, String color) {
        if (name != null) this.name = name;
        if (color != null) this.color = color;
    }
}
