package com.devroller.domain.hidden.service;

import com.devroller.domain.hidden.dto.HiddenIdeaResponse;
import com.devroller.domain.hidden.entity.HiddenIdea;
import com.devroller.domain.hidden.repository.HiddenIdeaRepository;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HiddenIdeaService {

    private final HiddenIdeaRepository hiddenIdeaRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;

    /**
     * 아이디어 숨기기
     */
    @Transactional
    public void hideIdea(Long userId, Long ideaId, String reason) {
        // 중복 체크
        if (hiddenIdeaRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_HIDDEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));

        // 숨김 저장
        HiddenIdea hiddenIdea = HiddenIdea.of(user, idea, reason);
        hiddenIdeaRepository.save(hiddenIdea);
    }

    /**
     * 아이디어 숨김 해제
     */
    @Transactional
    public void unhideIdea(Long userId, Long ideaId) {
        HiddenIdea hiddenIdea = hiddenIdeaRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HIDDEN_NOT_FOUND));

        hiddenIdeaRepository.delete(hiddenIdea);
    }

    /**
     * 내가 숨긴 아이디어 목록
     */
    public List<HiddenIdeaResponse> getMyHiddenIdeas(Long userId) {
        return hiddenIdeaRepository.findByUserId(userId).stream()
                .map(HiddenIdeaResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 아이디어 숨김 여부 확인
     */
    public boolean isHidden(Long userId, Long ideaId) {
        return hiddenIdeaRepository.existsByUserIdAndIdeaId(userId, ideaId);
    }
}
