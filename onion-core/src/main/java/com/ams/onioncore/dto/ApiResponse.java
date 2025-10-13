package com.ams.onioncore.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ApiResponse<T> {

    private boolean success;      // 성공 여부
    private int status;           // HTTP 상태 코드
    private String message;       // 응답 메시지
    private T data;               // 응답 데이터
    private LocalDateTime timestamp; // 응답 시각

    // 성공 응답 (데이터 + 메시지)
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 (데이터만)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 성공 응답 (메시지만)
    public static ApiResponse<Void> successMessage(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 실패 응답
    public static ApiResponse<Void> fail(HttpStatus status, String message) {
        return ApiResponse.<Void>builder()
                .success(false)
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}