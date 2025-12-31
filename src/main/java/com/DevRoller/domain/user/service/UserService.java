package com.devroller.domain.user.service;

import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.repository.TitleRepository;
import com.devroller.domain.user.dto.UserResponse;
import com.devroller.domain.user.dto.UserUpdateRequest;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TitleRepository titleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * ID로 사용자 조회
     */
    public UserResponse getUserById(Long userId) {
        User user = findUserById(userId);
        String titleName = getTitleName(user.getEquippedTitleId());
        return UserResponse.withTitle(user, titleName);
    }

    /**
     * 내 정보 조회
     */
    public UserResponse getMyInfo(Long userId) {
        return getUserById(userId);
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public UserResponse updateProfile(Long userId, UserUpdateRequest request) {
        User user = findUserById(userId);

        // 닉네임 중복 체크
        if (request.getNickname() != null && 
            !request.getNickname().equals(user.getNickname()) &&
            userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        user.updateProfile(request.getNickname(), request.getProfileImage());
        
        String titleName = getTitleName(user.getEquippedTitleId());
        return UserResponse.withTitle(user, titleName);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = findUserById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        user.changePassword(passwordEncoder.encode(newPassword));
    }

    /**
     * 칭호 장착
     */
    @Transactional
    public UserResponse equipTitle(Long userId, Long titleId) {
        User user = findUserById(userId);
        
        // 칭호 존재 확인
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TITLE_NOT_FOUND));

        user.equipTitle(titleId);
        return UserResponse.withTitle(user, title.getName());
    }

    /**
     * 레벨 랭킹 조회
     */
    public Page<UserResponse> getLevelRanking(Pageable pageable) {
        return userRepository.findByStatusOrderByLevelDescExperienceDesc(User.UserStatus.ACTIVE, pageable)
                .map(UserResponse::from);
    }

    /**
     * 완료 프로젝트 랭킹 조회
     */
    public Page<UserResponse> getCompletedRanking(Pageable pageable) {
        return userRepository.findByStatusOrderByTotalCompletedDesc(User.UserStatus.ACTIVE, pageable)
                .map(UserResponse::from);
    }

    /**
     * 스트릭 랭킹 조회
     */
    public Page<UserResponse> getStreakRanking(Pageable pageable) {
        return userRepository.findByStatusOrderByCurrentStreakDesc(User.UserStatus.ACTIVE, pageable)
                .map(UserResponse::from);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deactivateUser(Long userId) {
        User user = findUserById(userId);
        user.deactivate();
    }

    // ===== Private Methods =====

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private String getTitleName(Long titleId) {
        if (titleId == null) return null;
        return titleRepository.findById(titleId)
                .map(Title::getName)
                .orElse(null);
    }
}
