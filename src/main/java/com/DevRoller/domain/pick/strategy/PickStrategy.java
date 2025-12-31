package com.devroller.domain.pick.strategy;

import com.devroller.domain.idea.entity.Idea;

import java.util.List;

/**
 * 추첨 전략 인터페이스
 */
public interface PickStrategy {

    /**
     * 추첨 실행
     * @param ideas 추첨 대상 아이디어 목록
     * @return 선택된 아이디어
     */
    Idea pick(List<Idea> ideas);

    /**
     * 추첨 방식 이름
     */
    String getMethodName();
}