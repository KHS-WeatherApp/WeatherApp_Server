package com.server.KH_StudyProjects_WeatherServer.common.sidemenu.controller;

import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.service.SmFavoriteLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    /**
     * 사이드메뉴 즐겨찾기 위치 REST API 컨트롤러
     * 
     * Android 앱과의 HTTP 통신을 담당하며, 사이드메뉴 즐겨찾기 기능의 모든 API 엔드포인트 제공
     * 
     * API 경로: /api/sidemenu
     * 
     * 주요 기능:
     * - POST /locations: 새로운 즐겨찾기 위치 추가
     * - GET /locations: 사용자별 즐겨찾기 목록 조회
     * - DELETE /locations: 즐겨찾기 위치 삭제 (위도/경도 기반)
     * - PATCH /locations/sort-order: 정렬 순서 변경
     * - GET /locations/check-duplicate: 중복 위치 체크
     * 
     * 보안: 디바이스ID 기반 권한 체크로 사용자별 데이터 격리
     * 
     * @author 김효동
     * @since 2025-08-27
     */
@RestController
@RequestMapping("/api/sidemenu")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SmFavoriteLocationController {
    
    private final SmFavoriteLocationService smFavoriteLocationService;
    
    /**
     * 사이드메뉴 즐겨찾기 위치 추가 (사이드메뉴 검색 아이템 클릭시 호출)
     */
    @PostMapping("/locations")
    public ResponseEntity<SmFavoriteLocationResponseDto> addSmFavoriteLocation(
            @RequestBody SmFavoriteLocationRequestDto requestDto) {
        try {
            log.info("사이드메뉴 즐겨찾기 위치 추가 요청: {}", requestDto);
            
            // 필수 값 검증
            if (requestDto.getAddressName() == null || requestDto.getLatitude() == null || 
                requestDto.getLongitude() == null || requestDto.getDeviceId() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            SmFavoriteLocationResponseDto response = smFavoriteLocationService.addSmFavoriteLocation(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("사이드메뉴 즐겨찾기 위치 추가 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 사용자의 사이드메뉴 즐겨찾기 목록 조회
     */
    @GetMapping("/locations")
    public ResponseEntity<List<SmFavoriteLocationResponseDto>> getSmFavoriteLocations(
            @RequestParam String deviceId) {
        try {
            log.info("사이드메뉴 즐겨찾기 목록 조회 요청: deviceId={}", deviceId);
            
            List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationService.getSmFavoriteLocations(deviceId);
            return ResponseEntity.ok(locations);
            
        } catch (Exception e) {
            log.error("사이드메뉴 즐겨찾기 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 사이드메뉴 즐겨찾기 위치 삭제
     */
    @DeleteMapping("/locations")
    public ResponseEntity<Void> deleteSmFavoriteLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId) {
        try {
            log.info("사이드메뉴 즐겨찾기 위치 삭제 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
            
            smFavoriteLocationService.deleteSmFavoriteLocation(latitude, longitude, deviceId);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            log.error("사이드메뉴 즐겨찾기 위치 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

    
    /**
     * 사이드메뉴 즐겨찾기 위치 정렬 순서 변경
     */
    @PatchMapping("/locations/sort-order")
    public ResponseEntity<Void> updateSmFavoriteLocationSortOrder(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId,
            @RequestParam Integer sortOrder) {
        try {
            log.info("정렬 순서 변경 요청: lat={}, lng={}, deviceId={}, sortOrder={}", 
                    latitude, longitude, deviceId, sortOrder);
            
            smFavoriteLocationService.updateSmFavoriteLocationSortOrder(latitude, longitude, deviceId, sortOrder);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            log.error("정렬 순서 변경 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 사이드메뉴 즐겨찾기 중복 체크
     */
    @GetMapping("/locations/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicateLocation(
            @RequestParam Double latitude, 
            @RequestParam Double longitude, 
            @RequestParam String deviceId) {
        try {
            log.info("중복 체크 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
            
            boolean isDuplicate = smFavoriteLocationService.checkDuplicateLocation(latitude, longitude, deviceId);
            return ResponseEntity.ok(isDuplicate);
            
        } catch (Exception e) {
            log.error("중복 체크 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
