package com.devroller.domain.idea.entity;

import com.devroller.domain.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "idea_tags",
       uniqueConstraints = @UniqueConstraint(columnNames = {"idea_id", "tag_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    public IdeaTag(Idea idea, Tag tag) {
        this.idea = idea;
        this.tag = tag;
    }

    public static IdeaTag create(Idea idea, Tag tag) {
        IdeaTag ideaTag = new IdeaTag(idea, tag);
        idea.addTag(ideaTag);
        return ideaTag;
    }
}
