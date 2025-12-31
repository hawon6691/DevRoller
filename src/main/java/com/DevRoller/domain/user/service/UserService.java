package com.devroller.domain.user.service;

import com.devroller.domain.user.dto.UserProfileResponse;
import com.devroller.domain.user.dto.UserUpdateRequest;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 조회 (ID)
     */
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 사용자 프로필 조회
     */
    public UserProfileResponse getProfile(Long userId) {
        User user = findById(userId);
        return UserProfileResponse.from(user);
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UserUpdateRequest request) {
        User user = findById(userId);

        // 닉네임 변경 시 중복 체크
        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
            }
        }

        user.updateProfile(request.getNickname(), request.getProfileImage());
        log.info("User profile updated: {}", userId);

        return UserProfileResponse.from(user);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = findById(userId);

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);

        log.info("User password changed: {}", userId);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deactivate(Long userId, String password) {
        User user = findById(userId);

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        user.deactivate();
        log.info("User deactivated: {}", userId);
    }

    /**
     * 레벨 랭킹 조회
     */
    public List<UserProfileResponse> getLevelRanking(int limit) {
        return userRepository.findTopByLevelRanking(PageRequest.of(0, limit))
                .stream()
                .map(UserProfileResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 완료 프로젝트 랭킹 조회
     */
    public List<UserProfileResponse> getCompletedRanking(int limit) {
        return userRepository.findTopByCompletedRanking(PageRequest.of(0, limit))
                .stream()
                .map(UserProfileResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 스트릭 랭킹 조회
     */
    public List<UserProfileResponse> getStreakRanking(int limit) {
        return userRepository.findTopByStreakRanking(PageRequest.of(0, limit))
                .stream()
                .map(UserProfileResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 경험치 추가
     */
    @Transactional
    public void addExperience(Long userId, int exp) {
        User user = findById(userId);
        int beforeLevel = user.getLevel();
        user.addExperience(exp);
        
        if (user.getLevel() > beforeLevel) {
            log.info("User leveled up: {} (Lv.{} -> Lv.{})", userId, beforeLevel, user.getLevel());
        }
    }

    /**
     * 프로젝트 완료 처리
     */
    @Transactional
    public void completeProject(Long userId) {
        User user = findById(userId);
        user.completeProject();
        log.info("User completed project: {} (total: {})", userId, user.getTotalCompleted());
    }

    /**
     * 스트릭 초기화
     */
    @Transactional
    public void resetStreak(Long userId) {
        User user = findById(userId);
        user.resetStreak();
        log.info("User streak reset: {}", userId);
    }

    /**
     * 칭호 장착
     */
    @Transactional
    public void equipTitle(Long userId, Long titleId) {
        User user = findById(userId);
        user.equipTitle(titleId);
        log.info("User equipped title: {} -> titleId: {}", userId, titleId);
    }
}