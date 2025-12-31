package com.devroller.global.exception;

import com.devroller.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BusinessException 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException e, HttpServletRequest request) {
        
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Business Exception: {} - {} [{}]", 
                errorCode.getCode(), e.getMessage(), request.getRequestURI());
        
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), e.getMessage()));
    }

    /**
     * @Valid 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.warn("Validation Exception: {} [{}]", errors, request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                        .message("입력값 검증에 실패했습니다.")
                        .data(errors)
                        .build());
    }

    /**
     * @ModelAttribute 바인딩 실패 처리
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBindException(
            BindException e, HttpServletRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.warn("Bind Exception: {} [{}]", errors, request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                        .message("요청 파라미터 바인딩에 실패했습니다.")
                        .data(errors)
                        .build());
    }

    /**
     * 타입 변환 실패 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        
        String message = String.format("'%s' 파라미터의 타입이 올바르지 않습니다. (입력값: %s)", 
                e.getName(), e.getValue());
        
        log.warn("Type Mismatch Exception: {} [{}]", message, request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_TYPE_VALUE.getCode(), message));
    }

    /**
     * HTTP 메소드 불일치 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        
        log.warn("Method Not Allowed: {} [{}]", e.getMethod(), request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(
                        ErrorCode.METHOD_NOT_ALLOWED.getCode(),
                        String.format("'%s' 메소드는 지원하지 않습니다.", e.getMethod())));
    }

    /**
     * 인증 실패 처리
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {
        
        log.warn("Authentication Exception: {} [{}]", e.getMessage(), request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ErrorCode.UNAUTHORIZED.getCode(), "인증이 필요합니다."));
    }

    /**
     * 접근 거부 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        
        log.warn("Access Denied: {} [{}]", e.getMessage(), request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ErrorCode.ACCESS_DENIED.getCode(), "접근 권한이 없습니다."));
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception e, HttpServletRequest request) {
        
        log.error("Unexpected Exception: {} [{}]", e.getMessage(), request.getRequestURI(), e);
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        "서버 오류가 발생했습니다."));
    }
}
