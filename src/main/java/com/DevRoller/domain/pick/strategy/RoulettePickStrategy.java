package com.devroller.domain.pick.strategy;

import com.devroller.domain.idea.entity.Idea;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 룰렛 추첨 전략
 * 인기도(추첨 횟수)에 반비례하는 가중치로 선택
 * -> 덜 뽑힌 아이디어가 뽑힐 확률이 높음
 */
@Component
public class RoulettePickStrategy implements PickStrategy {

    private final Random random = new Random();

    @Override
    public Idea pick(List<Idea> ideas) {
        if (ideas.isEmpty()) {
            return null;
        }

        // 가중치 계산 (pickCount가 낮을수록 높은 가중치)
        int maxPickCount = ideas.stream()
                .mapToInt(Idea::getPickCount)
                .max()
                .orElse(0);

        // 총 가중치 합계 계산
        int totalWeight = 0;
        int[] weights = new int[ideas.size()];
        
        for (int i = 0; i < ideas.size(); i++) {
            // 역가중치: maxPickCount - pickCount + 1
            weights[i] = maxPickCount - ideas.get(i).getPickCount() + 1;
            totalWeight += weights[i];
        }

        // 룰렛 돌리기
        int randomValue = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (int i = 0; i < ideas.size(); i++) {
            cumulativeWeight += weights[i];
            if (randomValue < cumulativeWeight) {
                return ideas.get(i);
            }
        }

        // fallback
        return ideas.get(ideas.size() - 1);
    }

    @Override
    public String getMethodName() {
        return "ROULETTE";
    }
}