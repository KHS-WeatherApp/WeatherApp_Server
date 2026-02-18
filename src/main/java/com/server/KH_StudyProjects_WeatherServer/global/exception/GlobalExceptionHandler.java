package com.server.KH_StudyProjects_WeatherServer.global.exception;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.DuplicateLocationException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.LocationNotFoundException;
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

    /** 즐겨찾기 중복 예외 처리 (409) */
    @ExceptionHandler(DuplicateLocationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateLocationException(DuplicateLocationException e) {
        log.warn("중복 즐겨찾기 예외: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /** 즐겨찾기 미존재 예외 처리 (404) */
    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleLocationNotFoundException(LocationNotFoundException e) {
        log.warn("즐겨찾기 미존재 예외: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /** 외부 API 연동 예외 처리 (502/504) */
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleExternalApiException(ExternalApiException e) {
        log.error("외부 API 예외: status={}, message={}", e.getStatus(), e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    /** 런타임 예외 처리 (400) */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("런타임 예외: {}", e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /** 처리되지 않은 예외 처리 (500) */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("서버 예외: {}", e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.error("서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
