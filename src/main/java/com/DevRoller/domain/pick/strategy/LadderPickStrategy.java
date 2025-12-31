package com.devroller.domain.pick.strategy;

import com.devroller.domain.idea.entity.Idea;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 사다리 추첨 전략
 * 사다리 타기 시뮬레이션
 */
@Component
public class LadderPickStrategy implements PickStrategy {

    private final Random random = new Random();

    @Override
    public Idea pick(List<Idea> ideas) {
        if (ideas.isEmpty()) {
            return null;
        }

        int size = ideas.size();
        if (size == 1) {
            return ideas.get(0);
        }

        // 사다리 높이 (단계 수)
        int ladderHeight = Math.max(5, size * 2);
        
        // 사다리 가로선 생성 (각 단계마다 연결 여부)
        // bridges[row][col] = true면 col과 col+1이 연결됨
        boolean[][] bridges = new boolean[ladderHeight][size - 1];
        
        for (int row = 0; row < ladderHeight; row++) {
            for (int col = 0; col < size - 1; col++) {
                // 연속된 가로선 방지
                if (col > 0 && bridges[row][col - 1]) {
                    bridges[row][col] = false;
                } else {
                    bridges[row][col] = random.nextBoolean();
                }
            }
        }

        // 시작 위치 (랜덤)
        int position = random.nextInt(size);
        
        // 사다리 타기 실행
        for (int row = 0; row < ladderHeight; row++) {
            // 오른쪽 가로선 확인
            if (position < size - 1 && bridges[row][position]) {
                position++;
            }
            // 왼쪽 가로선 확인
            else if (position > 0 && bridges[row][position - 1]) {
                position--;
            }
        }

        return ideas.get(position);
    }

    @Override
    public String getMethodName() {
        return "LADDER";
    }
}