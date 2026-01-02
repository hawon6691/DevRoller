package com.devroller.domain.stats.dto;

import com.devroller.domain.idea.entity.Idea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularIdeasResponse {

    private List<PopularIdeaItem> byPickCount;
    private List<PopularIdeaItem> byCompleteCount;
    private List<PopularIdeaItem> byRating;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularIdeaItem {
        private Integer rank;
        private Long id;
        private String title;
        private String category;
        private Idea.Difficulty difficulty;
        private Integer pickCount;
        private Integer completeCount;
        private Double averageRating;

        public static PopularIdeaItem fromIdea(Idea idea, int rank) {
            return PopularIdeaItem.builder()
                    .rank(rank)
                    .id(idea.getId())
                    .title(idea.getTitle())
                    .category(idea.getCategory().getName())
                    .difficulty(idea.getDifficulty())
                    .pickCount(idea.getPickCount())
                    .completeCount(idea.getCompletedCount())
                    .averageRating(idea.getAverageRating())
                    .build();
        }
    }

    public static PopularIdeasResponse of(List<Idea> byPick, List<Idea> byComplete, List<Idea> byRating) {
        return PopularIdeasResponse.builder()
                .byPickCount(toRankedList(byPick))
                .byCompleteCount(toRankedList(byComplete))
                .byRating(toRankedList(byRating))
                .build();
    }

    private static List<PopularIdeaItem> toRankedList(List<Idea> ideas) {
        return ideas.stream()
                .map(idea -> PopularIdeaItem.fromIdea(idea, ideas.indexOf(idea) + 1))
                .collect(Collectors.toList());
    }
}
