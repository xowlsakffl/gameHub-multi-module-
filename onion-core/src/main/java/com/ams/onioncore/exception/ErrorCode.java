package com.ams.onioncore.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청 값입니다."),

    // 인증/인가
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // 파티 관련
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "파티를 찾을 수 없습니다."),
    REQUEST_ALREADY_APPROVED(HttpStatus.CONFLICT, "이미 승인된 요청입니다."),
    REQUEST_ALREADY_REJECTED(HttpStatus.CONFLICT, "이미 거절된 요청입니다."),
    ALREADY_PARTY_MEMBER(HttpStatus.CONFLICT, "이미 파티에 가입된 사용자입니다."),
    INVALID_JOIN_TYPE(HttpStatus.CONFLICT, "이 파티는 참가 요청이 불가능한 타입입니다."),

    NOT_PARTY_MEMBER(HttpStatus.BAD_REQUEST, "해당 사용자는 파티 멤버가 아닙니다."),
    CREATOR_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "방장은 파티를 나갈 수 없습니다."),
    CANNOT_KICK_LEADER(HttpStatus.BAD_REQUEST, "방장은 강퇴할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
