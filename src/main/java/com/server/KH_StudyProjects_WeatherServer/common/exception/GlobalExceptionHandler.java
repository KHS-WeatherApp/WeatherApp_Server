package com.server.KH_StudyProjects_WeatherServer.common.exception;

import com.server.KH_StudyProjects_WeatherServer.common.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 핸들러
 * 
 * 애플리케이션 전체에서 발생하는 모든 예외를 중앙에서 처리하여 일관된 에러 응답을 제공
 * @RestControllerAdvice를 사용하여 모든 컨트롤러의 예외를 자동으로 캐치
 * 
 * 처리하는 예외 유형:
 * - RuntimeException: 비즈니스 로직 관련 예외 (400 Bad Request)
 * - Exception: 일반적인 시스템 예외 (500 Internal Server Error)
 * 
 * 응답 형식:
 * - 모든 에러 응답은 ApiResponse 형식으로 표준화
 * - 적절한 HTTP 상태 코드와 함께 에러 메시지 전달
 * - 로그를 통한 에러 추적 및 디버깅 지원
 * 
 * @author 김효동
 * @since 2025-08-27
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * RuntimeException 처리 (비즈니스 로직 예외)
     * 
     * 서비스 레이어에서 발생하는 비즈니스 로직 관련 예외를 처리
     * 주로 사용자 입력 오류, 중복 데이터, 권한 부족 등으로 발생
     * 
     * 처리 예시:
     * - "이미 사이드메뉴 즐겨찾기에 등록된 위치입니다"
     * - "삭제 권한이 없습니다"
     * - "사이드메뉴 즐겨찾기 위치를 찾을 수 없습니다"
     * 
     * @param e 발생한 RuntimeException
     * @return ResponseEntity<ApiResponse<Void>> 400 Bad Request 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException 발생: {}", e.getMessage(), e);
        
        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * 일반 Exception 처리 (시스템 예외)
     * 
     * 예상치 못한 시스템 오류나 기술적 문제로 발생하는 예외를 처리
     * 데이터베이스 연결 실패, 외부 API 호출 실패, 메모리 부족 등
     * 
     * 보안 고려사항:
     * - 내부 시스템 정보를 클라이언트에게 노출하지 않음
     * - 일반적인 에러 메시지로 대체하여 응답
     * - 상세한 에러 정보는 로그에만 기록
     * 
     * @param e 발생한 Exception
     * @return ResponseEntity<ApiResponse<Void>> 500 Internal Server Error 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception 발생: {}", e.getMessage(), e);
        
        ApiResponse<Void> response = ApiResponse.error("서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
