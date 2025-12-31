package com.devroller.domain.pick.strategy;

import com.devroller.domain.idea.entity.Idea;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 제비뽑기 추첨 전략
 * 난이도에 따른 가중치 적용
 * EASY: 1, MEDIUM: 2, HARD: 3
 */
@Component
public class LotteryPickStrategy implements PickStrategy {

    private final Random random = new Random();

    @Override
    public Idea pick(List<Idea> ideas) {
        if (ideas.isEmpty()) {
            return null;
        }

        // 제비 목록 생성 (난이도별 가중치 적용)
        List<Idea> lottery = new ArrayList<>();
        
        for (Idea idea : ideas) {
            int tickets = getTicketCount(idea.getDifficulty());
            for (int i = 0; i < tickets; i++) {
                lottery.add(idea);
            }
        }

        // 섞기
        Collections.shuffle(lottery, random);

        // 제비 뽑기
        return lottery.get(random.nextInt(lottery.size()));
    }

    private int getTicketCount(Idea.Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 1;
            case MEDIUM -> 2;
            case HARD -> 3;
        };
    }

    @Override
    public String getMethodName() {
        return "LOTTERY";
    }
}