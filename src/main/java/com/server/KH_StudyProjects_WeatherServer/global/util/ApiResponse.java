package com.server.KH_StudyProjects_WeatherServer.global.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 공통 API 응답 래퍼 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /** 요청 성공 여부 */
    private boolean success;
    /** 응답 메시지 */
    private String message;
    /** 응답 데이터 */
    private T data;
    /** 응답 시각(Unix timestamp) */
    private long timestamp;

    /** 기본 성공 응답 생성 */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("성공")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /** 메시지를 포함한 성공 응답 생성 */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /** 실패 응답 생성 */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
