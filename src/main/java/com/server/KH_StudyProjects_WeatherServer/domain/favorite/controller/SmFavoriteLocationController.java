package com.server.KH_StudyProjects_WeatherServer.domain.favorite.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.service.SmFavoriteLocationService;
import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 사이드메뉴 즐겨찾기 REST 컨트롤러 */
@RestController
@RequestMapping("/api/sidemenu")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SmFavoriteLocationController {

    private final SmFavoriteLocationService smFavoriteLocationService;

    /** 즐겨찾기 위치 추가 */
    @PostMapping("/locations")
    public ResponseEntity<ApiResponse<SmFavoriteLocationResponseDto>> addSmFavoriteLocation(
            @RequestBody SmFavoriteLocationRequestDto requestDto) {
        log.info("즐겨찾기 추가 요청: {}", requestDto);

        if (requestDto.getAddressName() == null
                || requestDto.getLatitude() == null
                || requestDto.getLongitude() == null
                || requestDto.getDeviceId() == null) {
            return ResponseEntity.badRequest().build();
        }

        SmFavoriteLocationResponseDto response = smFavoriteLocationService.addSmFavoriteLocation(requestDto);
        String successMessage = requestDto.getAddressName() + "이(가) 즐겨찾기에 추가되었습니다.";
        ApiResponse<SmFavoriteLocationResponseDto> apiResponse = ApiResponse.success(successMessage, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /** 디바이스별 즐겨찾기 목록 조회 */
    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<SmFavoriteLocationResponseDto>>> getSmFavoriteLocations(
            @RequestParam String deviceId) {
        log.info("즐겨찾기 목록 조회 요청: deviceId={}", deviceId);

        List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationService.getSmFavoriteLocations(deviceId);
        ApiResponse<List<SmFavoriteLocationResponseDto>> apiResponse =
                ApiResponse.success("즐겨찾기 목록을 성공적으로 조회했습니다.", locations);
        return ResponseEntity.ok(apiResponse);
    }

    /** 즐겨찾기 위치 삭제 */
    @DeleteMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> deleteSmFavoriteLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId) {
        log.info("즐겨찾기 삭제 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);

        String deletedAddressName = smFavoriteLocationService.deleteSmFavoriteLocation(latitude, longitude, deviceId);
        String successMessage = deletedAddressName + "을(를) 즐겨찾기에서 삭제했습니다.";
        ApiResponse<Void> apiResponse = ApiResponse.success(successMessage, null);
        return ResponseEntity.ok(apiResponse);
    }

    /** 즐겨찾기 정렬 순서 변경 */
    @PatchMapping("/locations/sort-order")
    public ResponseEntity<ApiResponse<Void>> updateSmFavoriteLocationSortOrder(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId,
            @RequestParam Integer sortOrder) {
        log.info("정렬 순서 변경 요청: lat={}, lng={}, deviceId={}, sortOrder={}",
                latitude, longitude, deviceId, sortOrder);

        smFavoriteLocationService.updateSmFavoriteLocationSortOrder(latitude, longitude, deviceId, sortOrder);
        ApiResponse<Void> apiResponse = ApiResponse.success("정렬 순서가 성공적으로 변경되었습니다.", null);
        return ResponseEntity.ok(apiResponse);
    }

    /** 즐겨찾기 중복 여부 확인 */
    @GetMapping("/locations/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicateLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId) {
        log.info("중복 확인 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);

        boolean isDuplicate = smFavoriteLocationService.checkDuplicateLocation(latitude, longitude, deviceId);
        return ResponseEntity.ok(isDuplicate);
    }
}
