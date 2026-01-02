package com.devroller.domain.stats.dto;

import com.devroller.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRankingResponse {

    private List<RankingUser> rankings;
    private MyRank myRank;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingUser {
        private Integer rank;
        private Long userId;
        private String nickname;
        private String equippedTitle;
        private Integer level;
        private Integer completedCount;
        private Integer currentStreak;

        public static RankingUser fromUser(User user, int rank, String titleName) {
            return RankingUser.builder()
                    .rank(rank)
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .equippedTitle(titleName)
                    .level(user.getLevel())
                    .completedCount(user.getTotalCompleted())
                    .currentStreak(user.getCurrentStreak())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyRank {
        private Integer rank;
        private Integer totalUsers;
        private Double percentile;

        public static MyRank of(int rank, int total) {
            double percentile = total > 0 ? (double) (total - rank + 1) / total * 100 : 0.0;
            return MyRank.builder()
                    .rank(rank)
                    .totalUsers(total)
                    .percentile(Math.round(percentile * 100.0) / 100.0)
                    .build();
        }
    }
}
