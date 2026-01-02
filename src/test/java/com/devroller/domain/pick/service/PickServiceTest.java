package com.devroller.domain.pick.service;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.repository.IdeaRepository;
import com.devroller.domain.pick.dto.PickRequest;
import com.devroller.domain.pick.dto.PickResponse;
import com.devroller.domain.pick.entity.PickHistory;
import com.devroller.domain.pick.repository.PickHistoryRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PickService 테스트")
class PickServiceTest {

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PickHistoryRepository pickHistoryRepository;

    @InjectMocks
    private PickService pickService;

    private User testUser;
    private Category testCategory;
    private List<Idea> testIdeas;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("picker@test.com")
                .password("password")
                .nickname("피커")
                .build();

        testCategory = Category.builder()
                .name("웹 개발")
                .description("웹 개발")
                .build();

        Idea idea1 = Idea.builder()
                .title("Todo App")
                .category(testCategory)
                .difficulty(Idea.Difficulty.EASY)
                .build();

        Idea idea2 = Idea.builder()
                .title("Blog Platform")
                .category(testCategory)
                .difficulty(Idea.Difficulty.MEDIUM)
                .build();

        Idea idea3 = Idea.builder()
                .title("E-commerce")
                .category(testCategory)
                .difficulty(Idea.Difficulty.HARD)
                .build();

        testIdeas = Arrays.asList(idea1, idea2, idea3);
    }

    @Nested
    @DisplayName("랜덤 추첨")
    class RandomPick {

        @Test
        @DisplayName("랜덤 추첨 성공")
        void pickRandom_Success() {
            // given
            given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
            given(ideaRepository.findByIsActiveTrue()).willReturn(testIdeas);
            given(pickHistoryRepository.save(any(PickHistory.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            PickRequest request = new PickRequest();
            request.setMethod(PickHistory.PickMethod.RANDOM);

            // when
            PickResponse response = pickService.pick(1L, request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getIdea()).isNotNull();
            assertThat(testIdeas.stream().map(Idea::getTitle))
                    .contains(response.getIdea().getTitle());
        }

        @Test
        @DisplayName("추첨 가능한 아이디어 없을 때 예외")
        void pickRandom_NoAvailableIdeas() {
            // given
            given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
            given(ideaRepository.findByIsActiveTrue()).willReturn(Collections.emptyList());

            PickRequest request = new PickRequest();
            request.setMethod(PickHistory.PickMethod.RANDOM);

            // when & then
            assertThatThrownBy(() -> pickService.pick(1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NO_AVAILABLE_IDEAS);
        }
    }

    @Nested
    @DisplayName("필터 적용 추첨")
    class FilteredPick {

        @Test
        @DisplayName("난이도 필터 적용")
        void pickWithDifficultyFilter() {
            // given
            List<Idea> easyIdeas = testIdeas.stream()
                    .filter(i -> i.getDifficulty() == Idea.Difficulty.EASY)
                    .toList();

            given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
            given(ideaRepository.findByDifficultyAndIsActiveTrue(Idea.Difficulty.EASY))
                    .willReturn(easyIdeas);
            given(pickHistoryRepository.save(any(PickHistory.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            PickRequest request = new PickRequest();
            request.setMethod(PickHistory.PickMethod.RANDOM);
            request.setDifficulty("EASY");

            // when
            PickResponse response = pickService.pick(1L, request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getIdea().getDifficulty()).isEqualTo("EASY");
        }
    }

    @Nested
    @DisplayName("추첨 기록")
    class PickHistoryTest {

        @Test
        @DisplayName("추첨시 기록 저장")
        void savePickHistory() {
            // given
            given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
            given(ideaRepository.findByIsActiveTrue()).willReturn(testIdeas);
            given(pickHistoryRepository.save(any(com.devroller.domain.pick.entity.PickHistory.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            PickRequest request = new PickRequest();
            request.setMethod(com.devroller.domain.pick.entity.PickHistory.PickMethod.ROULETTE);

            // when
            pickService.pick(1L, request);

            // then
            verify(pickHistoryRepository, times(1)).save(any());
        }
    }
}
