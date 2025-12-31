package com.devroller.global.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 예외
 * ErrorCode를 기반으로 일관된 예외 처리
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    // 편의 메소드: 엔티티를 찾을 수 없을 때
    public static BusinessException notFound(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }

    // 편의 메소드: 중복 데이터
    public static BusinessException conflict(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }

    // 편의 메소드: 권한 없음
    public static BusinessException forbidden(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }
}
