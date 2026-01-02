package com.devroller.domain.like.service;

import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.like.dto.IdeaLikeResponse;
import com.devroller.domain.like.entity.IdeaLike;
import com.devroller.domain.like.repository.IdeaLikeRepository;
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
public class IdeaLikeService {

    private final IdeaLikeRepository ideaLikeRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;

    /**
     * 좋아요 추가
     */
    @Transactional
    public void addLike(Long userId, Long ideaId) {
        // 중복 체크
        if (ideaLikeRepository.existsByUserIdAndIdeaId(userId, ideaId)) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IDEA_NOT_FOUND));

        // 좋아요 저장
        IdeaLike ideaLike = IdeaLike.of(user, idea);
        ideaLikeRepository.save(ideaLike);

        // 카운트 증가
        idea.incrementLikeCount();
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void removeLike(Long userId, Long ideaId) {
        IdeaLike ideaLike = ideaLikeRepository.findByUserIdAndIdeaId(userId, ideaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        Idea idea = ideaLike.getIdea();

        // 좋아요 삭제
        ideaLikeRepository.delete(ideaLike);

        // 카운트 감소
        idea.decrementLikeCount();
    }

    /**
     * 내가 좋아요한 아이디어 목록
     */
    public List<IdeaLikeResponse> getMyLikes(Long userId) {
        return ideaLikeRepository.findByUserId(userId).stream()
                .map(IdeaLikeResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 아이디어 좋아요 여부 확인
     */
    public boolean isLiked(Long userId, Long ideaId) {
        return ideaLikeRepository.existsByUserIdAndIdeaId(userId, ideaId);
    }

    /**
     * 특정 아이디어의 좋아요 수
     */
    public long getLikeCount(Long ideaId) {
        return ideaLikeRepository.countByIdeaId(ideaId);
    }
}
