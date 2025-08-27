package com.server.KH_StudyProjects_WeatherServer.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 응답 표준화 래퍼 클래스
 * 
 * 모든 REST API 엔드포인트에서 일관된 응답 형식을 제공하기 위한 공통 응답 클래스
 * 성공/실패 여부, 메시지, 데이터, 타임스탬프를 포함한 표준화된 응답 구조
 * 
 * 사용 예시:
 * - 성공 응답: ApiResponse.success(data) 또는 ApiResponse.success("메시지", data)
 * - 실패 응답: ApiResponse.error("에러 메시지")
 * 
 * 응답 형식:
 * {
 *   "success": true/false,
 *   "message": "응답 메시지",
 *   "data": { ... },
 *   "timestamp": 1234567890
 * }
 * 
 * @author 김효동
 * @since 2025-08-27
 * @param <T> 응답 데이터의 타입
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success; // api 요청 처리 성공/실패 여부 true: 성공, false: 실패
    private String message; // 응답 메시지
    private T data; // 응답 데이터
    private long timestamp; // 응답 생성 시간 (Unix timestamp)
    
    /**
     * 성공 응답 생성 (기본 메시지)
     * 
     * 데이터만 전달하여 성공 응답을 생성할 때 사용
     * 메시지는 기본값 "성공"으로 설정
     * 
     * @param data 응답할 데이터
     * @return ApiResponse<T> 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("성공")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * 성공 응답 생성 (사용자 정의 메시지)
     * 
     * 데이터와 함께 사용자가 원하는 메시지를 포함한 성공 응답을 생성할 때 사용
     * 예: "위치가 성공적으로 저장되었습니다", "즐겨찾기가 추가되었습니다"
     * 
     * @param message 사용자 정의 성공 메시지
     * @param data 응답할 데이터
     * @return ApiResponse<T> 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * 에러 응답 생성
     * 
     * API 요청 처리 중 발생한 에러에 대한 응답을 생성할 때 사용
     * 에러 메시지를 포함하여 클라이언트에게 실패 원인을 명확히 전달
     * 
     * @param message 에러 내용을 설명하는 메시지
     * @return ApiResponse<T> 에러 응답 객체 (data는 null)
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
