package com.devroller.domain.user.repository;

import com.devroller.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일로 사용자 조회")
    void findByEmail() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .nickname("테스터")
                .build();
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getNickname()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("닉네임으로 사용자 조회")
    void findByNickname() {
        // given
        User user = User.builder()
                .email("nick@example.com")
                .password("password")
                .nickname("유니크닉네임")
                .build();
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByNickname("유니크닉네임");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("nick@example.com");
    }

    @Test
    @DisplayName("이메일 중복 체크")
    void existsByEmail() {
        // given
        User user = User.builder()
                .email("exists@example.com")
                .password("password")
                .nickname("존재자")
                .build();
        userRepository.save(user);

        // when & then
        assertThat(userRepository.existsByEmail("exists@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("notexists@example.com")).isFalse();
    }

    @Test
    @DisplayName("스트릭이 있는 사용자 조회")
    void findByCurrentStreakGreaterThan() {
        // given
        User user1 = User.builder()
                .email("streak1@test.com").password("pw").nickname("스트릭1").build();
        User user2 = User.builder()
                .email("streak2@test.com").password("pw").nickname("스트릭2").build();
        User user3 = User.builder()
                .email("nostreak@test.com").password("pw").nickname("노스트릭").build();

        user1.completeProject();  // streak = 1
        user2.completeProject();
        user2.completeProject();  // streak = 2
        // user3은 스트릭 0

        userRepository.saveAll(List.of(user1, user2, user3));

        // when
        List<User> usersWithStreak = userRepository.findByCurrentStreakGreaterThan(0);

        // then
        assertThat(usersWithStreak).hasSize(2);
        assertThat(usersWithStreak).extracting(User::getNickname)
                .containsExactlyInAnyOrder("스트릭1", "스트릭2");
    }
}
