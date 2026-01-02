package com.devroller.domain.gamification.title.service;

import com.devroller.domain.gamification.title.dto.TitleResponse;
import com.devroller.domain.gamification.title.dto.UserTitleResponse;
import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.entity.UserTitle;
import com.devroller.domain.gamification.title.repository.TitleRepository;
import com.devroller.domain.gamification.title.repository.UserTitleRepository;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.service.UserService;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 칭호 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TitleService {

    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserService userService;

    /**
     * 전체 칭호 목록
     */
    public List<TitleResponse> getAllTitles() {
        return titleRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(TitleResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 타입별 칭호 목록
     */
    public List<TitleResponse> getTitlesByType(Title.TitleType type) {
        return titleRepository.findByTypeOrderByDisplayOrderAsc(type)
                .stream()
                .map(TitleResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 보유 칭호 목록
     */
    public List<UserTitleResponse> getUserTitles(Long userId) {
        return userTitleRepository.findByUserIdOrderByAcquiredAtDesc(userId)
                .stream()
                .map(UserTitleResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 장착된 칭호
     */
    public Optional<UserTitleResponse> getEquippedTitle(Long userId) {
        return userTitleRepository.findByUserIdAndIsEquippedTrue(userId)
                .map(UserTitleResponse::from);
    }

    /**
     * 칭호 장착
     */
    @Transactional
    public void equipTitle(Long userId, Long titleId) {
        // 칭호 보유 확인
        UserTitle userTitle = userTitleRepository.findByUserIdAndTitleId(userId, titleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TITLE_NOT_OWNED));

        // 기존 장착 해제
        userTitleRepository.unequipAllByUserId(userId);

        // 새 칭호 장착
        userTitle.equip();

        // User 엔티티에도 반영
        userService.equipTitle(userId, titleId);

        log.info("User {} equipped title {}", userId, titleId);
    }

    /**
     * 칭호 해제
     */
    @Transactional
    public void unequipTitle(Long userId) {
        userTitleRepository.unequipAllByUserId(userId);
        userService.equipTitle(userId, null);
        log.info("User {} unequipped title", userId);
    }

    /**
     * 레벨업 시 칭호 확인 및 부여
     */
    @Transactional
    public void checkAndGrantLevelTitle(Long userId, int newLevel) {
        // 해당 레벨에서 획득 가능한 칭호 조회
        List<Title> titles = titleRepository.findByTypeAndRequiredLevel(Title.TitleType.LEVEL, newLevel);
        if (!titles.isEmpty()) {
            grantTitle(userId, titles.get(0));
        }
    }

    /**
     * 업적 달성 시 칭호 확인 및 부여
     */
    @Transactional
    public void checkAndGrantAchievementTitle(Long userId, String achievementCode) {
        titleRepository.findByRequiredAchievementCode(achievementCode)
                .ifPresent(title -> grantTitle(userId, title));
    }

    /**
     * 칭호 부여
     */
    @Transactional
    public void grantTitle(Long userId, Title title) {
        // 이미 보유중인지 확인
        if (userTitleRepository.existsByUserIdAndTitleId(userId, title.getId())) {
            return;
        }

        User user = userService.findById(userId);
        UserTitle userTitle = UserTitle.builder()
                .user(user)
                .title(title)
                .build();

        userTitleRepository.save(userTitle);
        log.info("User {} granted title: {}", userId, title.getName());
    }

    /**
     * 사용자의 칭호 보유 수
     */
    public long getTitleCount(Long userId) {
        return userTitleRepository.countByUserId(userId);
    }

    /**
     * 최근 획득한 칭호
     */
    public List<UserTitleResponse> getRecentTitles(Long userId, int limit) {
        return userTitleRepository.findRecentTitles(userId, PageRequest.of(0, limit))
                .stream()
                .map(UserTitleResponse::from)
                .collect(Collectors.toList());
    }
}