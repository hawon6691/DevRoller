package com.devroller.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * API 공통 응답 포맷
 * 
 * 성공 예시:
 * {
 *   "success": true,
 *   "message": "Success",
 *   "data": { ... },
 *   "timestamp": "2025-01-01T12:00:00"
 * }
 * 
 * 실패 예시:
 * {
 *   "success": false,
 *   "message": "User not found",
 *   "code": "USER_NOT_FOUND",
 *   "timestamp": "2025-01-01T12:00:00"
 * }
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final String code;
    private final T data;
    private final LocalDateTime timestamp;

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 (메시지 + 데이터)
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 (메시지만)
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답 (메시지만)
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답 (코드 + 메시지)
    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 생성 성공 응답 (201)
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Created successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
