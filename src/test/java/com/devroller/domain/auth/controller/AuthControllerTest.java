package com.devroller.domain.auth.controller;

import com.devroller.domain.auth.dto.LoginRequest;
import com.devroller.domain.auth.dto.SignupRequest;
import com.devroller.domain.auth.dto.TokenResponse;
import com.devroller.domain.auth.service.AuthService;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@DisplayName("AuthController 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @Test
        @WithMockUser
        @DisplayName("회원가입 성공")
        void signup_Success() throws Exception {
            // given
            SignupRequest request = new SignupRequest();
            request.setEmail("test@example.com");
            request.setPassword("password123");
            request.setNickname("테스터");

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken("access-token")
                    .refreshToken("refresh-token")
                    .build();

            given(authService.signup(any(SignupRequest.class))).willReturn(tokenResponse);

            // when & then
            mockMvc.perform(post("/api/v1/auth/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.accessToken").value("access-token"));
        }

        @Test
        @WithMockUser
        @DisplayName("중복 이메일로 회원가입 실패")
        void signup_DuplicateEmail() throws Exception {
            // given
            SignupRequest request = new SignupRequest();
            request.setEmail("existing@example.com");
            request.setPassword("password123");
            request.setNickname("테스터");

            given(authService.signup(any(SignupRequest.class)))
                    .willThrow(new BusinessException(ErrorCode.DUPLICATE_EMAIL));

            // when & then
            mockMvc.perform(post("/api/v1/auth/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error.code").value("U002"));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @Test
        @WithMockUser
        @DisplayName("로그인 성공")
        void login_Success() throws Exception {
            // given
            LoginRequest request = new LoginRequest();
            request.setEmail("test@example.com");
            request.setPassword("password123");

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken("access-token")
                    .refreshToken("refresh-token")
                    .build();

            given(authService.login(any(LoginRequest.class))).willReturn(tokenResponse);

            // when & then
            mockMvc.perform(post("/api/v1/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.accessToken").exists());
        }

        @Test
        @WithMockUser
        @DisplayName("잘못된 비밀번호로 로그인 실패")
        void login_InvalidPassword() throws Exception {
            // given
            LoginRequest request = new LoginRequest();
            request.setEmail("test@example.com");
            request.setPassword("wrongPassword");

            given(authService.login(any(LoginRequest.class)))
                    .willThrow(new BusinessException(ErrorCode.INVALID_PASSWORD));

            // when & then
            mockMvc.perform(post("/api/v1/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error.code").value("U004"));
        }
    }
}
