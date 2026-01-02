package com.devroller.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common (C)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 타입입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C005", "허용되지 않은 메소드입니다."),

    // Auth (A)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A004", "접근 권한이 없습니다."),

    // User (U)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U002", "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "U003", "이미 존재하는 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U004", "비밀번호가 일치하지 않습니다."),

    // Idea (I)
    IDEA_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "아이디어를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "I002", "카테고리를 찾을 수 없습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "I003", "태그를 찾을 수 없습니다."),
    NO_AVAILABLE_IDEA(HttpStatus.NOT_FOUND, "I004", "추첨 가능한 아이디어가 없습니다."),

    // Pick (P)
    PICK_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "추첨 기록을 찾을 수 없습니다."),
    INVALID_PICK_METHOD(HttpStatus.BAD_REQUEST, "P002", "잘못된 추첨 방식입니다."),
    ALREADY_IN_PROGRESS(HttpStatus.CONFLICT, "P003", "이미 진행 중인 프로젝트입니다."),
    ALREADY_COMPLETED(HttpStatus.CONFLICT, "P004", "이미 완료된 프로젝트입니다."),
    NO_AVAILABLE_IDEAS(HttpStatus.NOT_FOUND, "P005", "추첨 가능한 아이디어가 없습니다."),
    PICK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "P006", "추첨에 실패했습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "P007", "프로젝트를 찾을 수 없습니다."),
    NOT_COMPLETED(HttpStatus.BAD_REQUEST, "P008", "완료되지 않은 프로젝트입니다."),

    // Gamification (G)
    ACHIEVEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "G001", "업적을 찾을 수 없습니다."),
    TITLE_NOT_FOUND(HttpStatus.NOT_FOUND, "G002", "칭호를 찾을 수 없습니다."),
    ALREADY_ACHIEVED(HttpStatus.CONFLICT, "G003", "이미 달성한 업적입니다."),
    TITLE_NOT_OWNED(HttpStatus.FORBIDDEN, "G004", "보유하지 않은 칭호입니다."),

    // Bookmark (B)
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "북마크를 찾을 수 없습니다."),
    ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "B002", "이미 북마크한 아이디어입니다."),

    // Review (R)
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "리뷰를 찾을 수 없습니다."),
    ALREADY_REVIEWED(HttpStatus.CONFLICT, "R002", "이미 리뷰를 작성했습니다."),
    REVIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN, "R003", "완료한 프로젝트만 리뷰할 수 있습니다."),

    // Suggestion (S)
    SUGGESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "제안을 찾을 수 없습니다."),

    // Like (L)
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "L001", "좋아요를 찾을 수 없습니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "L002", "이미 좋아요한 아이디어입니다."),

    // Hidden (H)
    HIDDEN_NOT_FOUND(HttpStatus.NOT_FOUND, "H001", "숨김을 찾을 수 없습니다."),
    ALREADY_HIDDEN(HttpStatus.CONFLICT, "H002", "이미 숨긴 아이디어입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
