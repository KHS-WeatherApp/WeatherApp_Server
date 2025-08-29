package com.server.KH_StudyProjects_WeatherServer.common.sidemenu.controller;

import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.service.SmFavoriteLocationService;
import com.server.KH_StudyProjects_WeatherServer.common.util.ApiResponse;
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
    public ResponseEntity<ApiResponse<SmFavoriteLocationResponseDto>> addSmFavoriteLocation(
            @RequestBody SmFavoriteLocationRequestDto requestDto) {
        log.info("사이드메뉴 즐겨찾기 위치 추가 요청: {}", requestDto);
        
        // 필수 값 검증
        if (requestDto.getAddressName() == null || requestDto.getLatitude() == null || 
            requestDto.getLongitude() == null || requestDto.getDeviceId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        SmFavoriteLocationResponseDto response = smFavoriteLocationService.addSmFavoriteLocation(requestDto);
        
        // 지역명을 포함한 성공 메시지 생성
        String successMessage = requestDto.getAddressName() + "이(가) 즐겨찾기에 추가되었습니다";
        ApiResponse<SmFavoriteLocationResponseDto> apiResponse = ApiResponse.success(successMessage, response);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    
    /**
     * 사용자의 사이드메뉴 즐겨찾기 목록 조회
     */
    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<SmFavoriteLocationResponseDto>>> getSmFavoriteLocations(
            @RequestParam String deviceId) {
        log.info("사이드메뉴 즐겨찾기 목록 조회 요청: deviceId={}", deviceId);
        
        List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationService.getSmFavoriteLocations(deviceId);
        ApiResponse<List<SmFavoriteLocationResponseDto>> apiResponse = ApiResponse.success("즐겨찾기 목록을 성공적으로 조회했습니다", locations);
        return ResponseEntity.ok(apiResponse);
    }
    
    /**
     * 사이드메뉴 즐겨찾기 위치 삭제
     */
    @DeleteMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> deleteSmFavoriteLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId) {
        log.info("사이드메뉴 즐겨찾기 위치 삭제 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
        
        String deletedAddressName = smFavoriteLocationService.deleteSmFavoriteLocation(latitude, longitude, deviceId);
        String successMessage = deletedAddressName + "을(를) 즐겨찾기에서 제거했습니다";
        ApiResponse<Void> apiResponse = ApiResponse.success(successMessage, null);
        return ResponseEntity.ok(apiResponse);
    }
    

    
    /**
     * 사이드메뉴 즐겨찾기 위치 정렬 순서 변경
     */
    @PatchMapping("/locations/sort-order")
    public ResponseEntity<ApiResponse<Void>> updateSmFavoriteLocationSortOrder(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId,
            @RequestParam Integer sortOrder) {
        log.info("정렬 순서 변경 요청: lat={}, lng={}, deviceId={}, sortOrder={}", 
                latitude, longitude, deviceId, sortOrder);
        
        smFavoriteLocationService.updateSmFavoriteLocationSortOrder(latitude, longitude, deviceId, sortOrder);
        ApiResponse<Void> apiResponse = ApiResponse.success("정렬 순서가 성공적으로 변경되었습니다", null);
        return ResponseEntity.ok(apiResponse);
    }
    
    /**
     * 사이드메뉴 즐겨찾기 중복 체크
     */
    @GetMapping("/locations/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicateLocation(
            @RequestParam Double latitude, 
            @RequestParam Double longitude, 
            @RequestParam String deviceId) {
        log.info("중복 체크 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
        
        boolean isDuplicate = smFavoriteLocationService.checkDuplicateLocation(latitude, longitude, deviceId);
        return ResponseEntity.ok(isDuplicate);
    }
}
