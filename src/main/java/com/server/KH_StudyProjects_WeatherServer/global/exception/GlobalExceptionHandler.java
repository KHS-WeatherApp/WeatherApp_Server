package com.server.KH_StudyProjects_WeatherServer.global.exception;

import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 전역 예외 처리 핸들러 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** 외부 API 연동 예외 처리 (502/504) */
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleExternalApiException(ExternalApiException e) {
        log.error("ExternalApiException 발생: status={}, message={}", e.getStatus(), e.getMessage(), e);

        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    /** 런타임 예외 처리 (400) */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException 발생: {}", e.getMessage(), e);

        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /** 처리되지 않은 예외 처리 (500) */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception 발생: {}", e.getMessage(), e);

        ApiResponse<Void> response = ApiResponse.error("서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
