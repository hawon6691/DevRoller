package com.devroller.domain.idea.service;

import com.devroller.domain.category.entity.Category;
import com.devroller.domain.category.repository.CategoryRepository;
import com.devroller.domain.idea.dto.IdeaResponse;
import com.devroller.domain.idea.entity.Idea;
import com.devroller.domain.idea.repository.IdeaRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IdeaService 테스트")
class IdeaServiceTest {

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private IdeaService ideaService;

    private Category testCategory;
    private Idea testIdea;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .name("웹 개발")
                .description("웹 개발 관련 프로젝트")
                .build();

        testIdea = Idea.builder()
                .title("Todo List 앱")
                .description("기본적인 할일 관리 앱")
                .category(testCategory)
                .difficulty(Idea.Difficulty.EASY)
                .estimatedHours(10)
                .build();
    }

    @Nested
    @DisplayName("아이디어 조회")
    class GetIdea {

        @Test
        @DisplayName("ID로 아이디어 조회 성공")
        void getIdeaById_Success() {
            // given
            given(ideaRepository.findById(1L)).willReturn(Optional.of(testIdea));

            // when
            IdeaResponse response = ideaService.getIdeaById(1L);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getTitle()).isEqualTo("Todo List 앱");
            assertThat(response.getDifficulty()).isEqualTo("EASY");
        }

        @Test
        @DisplayName("존재하지 않는 아이디어 조회시 예외 발생")
        void getIdeaById_NotFound() {
            // given
            given(ideaRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> ideaService.getIdeaById(999L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.IDEA_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("아이디어 목록 조회")
    class GetIdeas {

        @Test
        @DisplayName("전체 아이디어 페이징 조회")
        void getAllIdeas_Success() {
            // given
            Idea idea2 = Idea.builder()
                    .title("블로그 플랫폼")
                    .description("마크다운 지원 블로그")
                    .category(testCategory)
                    .difficulty(Idea.Difficulty.MEDIUM)
                    .build();

            List<Idea> ideas = Arrays.asList(testIdea, idea2);
            Page<Idea> ideaPage = new PageImpl<>(ideas);
            
            given(ideaRepository.findByIsActiveTrue(any(PageRequest.class)))
                    .willReturn(ideaPage);

            // when
            Page<IdeaResponse> result = ideaService.getAllIdeas(PageRequest.of(0, 10));

            // then
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getTitle()).isEqualTo("Todo List 앱");
        }
    }

    @Nested
    @DisplayName("아이디어 통계")
    class IdeaStats {

        @Test
        @DisplayName("Pick 카운트 증가")
        void incrementPickCount() {
            // given
            int initialCount = testIdea.getPickCount();

            // when
            testIdea.incrementPickCount();

            // then
            assertThat(testIdea.getPickCount()).isEqualTo(initialCount + 1);
        }

        @Test
        @DisplayName("완료 카운트 증가")
        void incrementCompletedCount() {
            // given
            int initialCount = testIdea.getCompletedCount();

            // when
            testIdea.incrementCompletedCount();

            // then
            assertThat(testIdea.getCompletedCount()).isEqualTo(initialCount + 1);
        }

        @Test
        @DisplayName("난이도별 경험치 계산")
        void getExperiencePoints() {
            // given
            Idea easyIdea = Idea.builder()
                    .title("Easy").category(testCategory).difficulty(Idea.Difficulty.EASY).build();
            Idea mediumIdea = Idea.builder()
                    .title("Medium").category(testCategory).difficulty(Idea.Difficulty.MEDIUM).build();
            Idea hardIdea = Idea.builder()
                    .title("Hard").category(testCategory).difficulty(Idea.Difficulty.HARD).build();

            // when & then
            assertThat(easyIdea.getExperiencePoints()).isEqualTo(50);
            assertThat(mediumIdea.getExperiencePoints()).isEqualTo(100);
            assertThat(hardIdea.getExperiencePoints()).isEqualTo(200);
        }
    }
}
