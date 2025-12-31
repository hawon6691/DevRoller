package com.devroller.domain.pick.strategy;

import com.devroller.domain.idea.entity.Idea;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 랜덤 추첨 전략
 * 완전 무작위로 하나를 선택
 */
@Component
public class RandomPickStrategy implements PickStrategy {

    private final Random random = new Random();

    @Override
    public Idea pick(List<Idea> ideas) {
        if (ideas.isEmpty()) {
            return null;
        }
        int index = random.nextInt(ideas.size());
        return ideas.get(index);
    }

    @Override
    public String getMethodName() {
        return "RANDOM";
    }
}