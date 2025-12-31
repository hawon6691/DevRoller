package com.devroller.domain.user.service;

import com.devroller.domain.user.dto.UserResponse;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .nickname("테스터")
                .build();
    }

    @Nested
    @DisplayName("사용자 조회")
    class GetUser {

        @Test
        @DisplayName("ID로 사용자 조회 성공")
        void getUserById_Success() {
            // given
            given(userRepository.findById(1L)).willReturn(Optional.of(testUser));

            // when
            UserResponse response = userService.getUserById(1L);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getEmail()).isEqualTo("test@example.com");
            assertThat(response.getNickname()).isEqualTo("테스터");
        }

        @Test
        @DisplayName("존재하지 않는 사용자 조회시 예외 발생")
        void getUserById_NotFound() {
            // given
            given(userRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.getUserById(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("경험치 & 레벨")
    class ExperienceAndLevel {

        @Test
        @DisplayName("경험치 추가 후 레벨업")
        void addExperience_LevelUp() {
            // given
            User user = User.builder()
                    .email("level@test.com")
                    .password("password")
                    .nickname("레벨러")
                    .build();
            // 초기 레벨 1, 경험치 0

            // when
            user.addExperience(150);  // 레벨 1 필요 경험치 100

            // then
            assertThat(user.getLevel()).isEqualTo(2);
            assertThat(user.getExperience()).isEqualTo(50);  // 150 - 100 = 50
        }

        @Test
        @DisplayName("다중 레벨업")
        void addExperience_MultipleLevelUp() {
            // given
            User user = User.builder()
                    .email("multi@test.com")
                    .password("password")
                    .nickname("멀티레벨러")
                    .build();

            // when
            user.addExperience(350);  // 레벨1: 100, 레벨2: 200 필요

            // then
            assertThat(user.getLevel()).isEqualTo(3);
            assertThat(user.getExperience()).isEqualTo(50);  // 350 - 100 - 200 = 50
        }
    }

    @Nested
    @DisplayName("스트릭")
    class Streak {

        @Test
        @DisplayName("프로젝트 완료시 스트릭 증가")
        void completeProject_IncrementStreak() {
            // given
            User user = User.builder()
                    .email("streak@test.com")
                    .password("password")
                    .nickname("스트리커")
                    .build();

            // when
            user.completeProject();
            user.completeProject();

            // then
            assertThat(user.getTotalCompleted()).isEqualTo(2);
            assertThat(user.getCurrentStreak()).isEqualTo(2);
            assertThat(user.getMaxStreak()).isEqualTo(2);
        }

        @Test
        @DisplayName("스트릭 리셋")
        void resetStreak() {
            // given
            User user = User.builder()
                    .email("reset@test.com")
                    .password("password")
                    .nickname("리셋터")
                    .build();
            user.completeProject();
            user.completeProject();
            user.completeProject();  // streak = 3, maxStreak = 3

            // when
            user.resetStreak();

            // then
            assertThat(user.getCurrentStreak()).isEqualTo(0);
            assertThat(user.getMaxStreak()).isEqualTo(3);  // maxStreak은 유지
        }
    }
}
