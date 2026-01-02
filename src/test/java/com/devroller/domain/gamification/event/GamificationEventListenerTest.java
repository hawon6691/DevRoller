package com.devroller.domain.gamification.event;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.gamification.achievement.entity.Achievement;
import com.devroller.domain.gamification.achievement.entity.UserAchievement;
import com.devroller.domain.gamification.achievement.repository.AchievementRepository;
import com.devroller.domain.gamification.achievement.repository.UserAchievementRepository;
import com.devroller.domain.gamification.title.entity.Title;
import com.devroller.domain.gamification.title.entity.UserTitle;
import com.devroller.domain.gamification.title.repository.TitleRepository;
import com.devroller.domain.gamification.title.repository.UserTitleRepository;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.user.entity.User;
import com.devroller.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GamificationEventListener 테스트")
class GamificationEventListenerTest {

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private TitleRepository titleRepository;
    @Mock
    private UserTitleRepository userTitleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private GamificationEventListener listener;

    private User testUser;
    private Idea testIdea;
    private Achievement testAchievement;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("gamer@test.com")
                .password("password")
                .nickname("게이머")
                .build();

        Category category = Category.builder()
                .name("웹 개발")
                .build();

        testIdea = Idea.builder()
                .title("테스트 프로젝트")
                .category(category)
                .difficulty(Idea.Difficulty.MEDIUM)
                .build();

        testAchievement = Achievement.builder()
                .code("FIRST_COMPLETE")
                .name("첫 번째 완료")
                .type(Achievement.AchievementType.COMPLETE_COUNT)
                .requiredValue(1)
                .rewardExp(100)
                .build();
    }

    @Nested
    @DisplayName("프로젝트 완료 이벤트")
    class ProjectCompletedEventTest {

        @Test
        @DisplayName("완료시 경험치 부여")
        void handleProjectCompleted_AddExperience() {
            // given
            given(achievementRepository.findByType(any())).willReturn(Collections.emptyList());
            given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

            ProjectCompletedEvent event = new ProjectCompletedEvent(this, testUser, testIdea);
            int initialExp = testUser.getExperience();

            // when
            listener.handleProjectCompleted(event);

            // then
            verify(userRepository).save(testUser);
            // Medium 난이도 = 100 경험치
            assertThat(testUser.getExperience()).isEqualTo(initialExp + 100);
        }

        @Test
        @DisplayName("업적 진행도 업데이트")
        void handleProjectCompleted_UpdateAchievementProgress() {
            // given
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(testUser)
                    .achievement(testAchievement)
                    .build();

            given(achievementRepository.findByType(Achievement.AchievementType.COMPLETE_COUNT))
                    .willReturn(Arrays.asList(testAchievement));
            given(userAchievementRepository.findByUserIdAndAchievementId(any(), any()))
                    .willReturn(Optional.of(userAchievement));
            given(userAchievementRepository.save(any())).willReturn(userAchievement);
            given(userRepository.save(any())).willReturn(testUser);

            testUser.completeProject();  // totalCompleted = 1

            ProjectCompletedEvent event = new ProjectCompletedEvent(this, testUser, testIdea);

            // when
            listener.handleProjectCompleted(event);

            // then
            verify(userAchievementRepository).save(any(UserAchievement.class));
        }
    }

    @Nested
    @DisplayName("레벨업 이벤트")
    class LevelUpEventTest {

        @Test
        @DisplayName("레벨업시 칭호 부여")
        void handleLevelUp_GrantTitle() {
            // given
            Title levelTitle = Title.builder()
                    .code("LV10_TITLE")
                    .name("성장하는 개발자")
                    .type(Title.TitleType.LEVEL)
                    .requiredLevel(10)
                    .build();

            given(titleRepository.findByTypeAndRequiredLevelLessThanEqual(Title.TitleType.LEVEL, 10))
                    .willReturn(Arrays.asList(levelTitle));
            given(userTitleRepository.existsByUserIdAndTitleId(any(), any()))
                    .willReturn(false);
            given(userTitleRepository.save(any(UserTitle.class)))
                    .willAnswer(inv -> inv.getArgument(0));

            LevelUpEvent event = new LevelUpEvent(this, testUser, 9, 10);

            // when
            listener.handleLevelUp(event);

            // then
            verify(userTitleRepository).save(any(UserTitle.class));
        }

        @Test
        @DisplayName("이미 보유한 칭호는 중복 부여하지 않음")
        void handleLevelUp_SkipExistingTitle() {
            // given
            Title levelTitle = Title.builder()
                    .code("LV5_TITLE")
                    .name("초보 개발자")
                    .type(Title.TitleType.LEVEL)
                    .requiredLevel(5)
                    .build();

            given(titleRepository.findByTypeAndRequiredLevelLessThanEqual(Title.TitleType.LEVEL, 10))
                    .willReturn(Arrays.asList(levelTitle));
            given(userTitleRepository.existsByUserIdAndTitleId(any(), any()))
                    .willReturn(true);  // 이미 보유

            LevelUpEvent event = new LevelUpEvent(this, testUser, 9, 10);

            // when
            listener.handleLevelUp(event);

            // then
            verify(userTitleRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("업적 달성 이벤트")
    class AchievementUnlockedEventTest {

        @Test
        @DisplayName("업적 달성시 연동 칭호 부여")
        void handleAchievementUnlocked_GrantLinkedTitle() {
            // given
            Title linkedTitle = Title.builder()
                    .code("FIRST_COMPLETE_TITLE")
                    .name("시작이 반이다")
                    .type(Title.TitleType.ACHIEVEMENT)
                    .requiredAchievementCode("FIRST_COMPLETE")
                    .build();

            given(titleRepository.findByRequiredAchievementCode("FIRST_COMPLETE"))
                    .willReturn(Optional.of(linkedTitle));
            given(userTitleRepository.existsByUserIdAndTitleId(any(), any()))
                    .willReturn(false);
            given(userTitleRepository.save(any(UserTitle.class)))
                    .willAnswer(inv -> inv.getArgument(0));
            given(userRepository.save(any())).willReturn(testUser);

            AchievementUnlockedEvent event = new AchievementUnlockedEvent(this, testUser, testAchievement);

            // when
            listener.handleAchievementUnlocked(event);

            // then
            verify(userTitleRepository).save(any(UserTitle.class));
        }

        @Test
        @DisplayName("업적 보상 경험치 부여")
        void handleAchievementUnlocked_AddRewardExp() {
            // given
            given(titleRepository.findByRequiredAchievementCode(any()))
                    .willReturn(Optional.empty());
            given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

            int initialExp = testUser.getExperience();
            AchievementUnlockedEvent event = new AchievementUnlockedEvent(this, testUser, testAchievement);

            // when
            listener.handleAchievementUnlocked(event);

            // then
            verify(userRepository).save(testUser);
            assertThat(testUser.getExperience()).isEqualTo(initialExp + 100);  // rewardExp = 100
        }
    }
}
