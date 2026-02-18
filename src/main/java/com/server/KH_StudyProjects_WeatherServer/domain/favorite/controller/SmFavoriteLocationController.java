package com.server.KH_StudyProjects_WeatherServer.domain.favorite.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.service.SmFavoriteLocationService;
import com.server.KH_StudyProjects_WeatherServer.global.logging.LogCode;
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
        log.info("[{}] favorite.controller.add.request endpoint=/api/sidemenu/locations deviceId={} latitude={} longitude={}",
                LogCode.FAV_REQ_001,
                requestDto.getDeviceId(),
                requestDto.getLatitude(),
                requestDto.getLongitude());

        if (requestDto.getAddressName() == null
                || requestDto.getLatitude() == null
                || requestDto.getLongitude() == null
                || requestDto.getDeviceId() == null) {
            log.warn("[{}] favorite.controller.add.invalid-request endpoint=/api/sidemenu/locations",
                    LogCode.FAV_400_001);
            ApiResponse<SmFavoriteLocationResponseDto> apiResponse =
                    ApiResponse.error("필수 요청값(addressName, latitude, longitude, deviceId)이 누락되었습니다.");
            return ResponseEntity.badRequest().body(apiResponse);
        }

        SmFavoriteLocationResponseDto response = smFavoriteLocationService.addSmFavoriteLocation(requestDto);
        String successMessage = requestDto.getAddressName() + "이(가) 즐겨찾기에 추가되었습니다.";
        ApiResponse<SmFavoriteLocationResponseDto> apiResponse = ApiResponse.success(successMessage, response);
        log.info("[{}] favorite.controller.add.success endpoint=/api/sidemenu/locations deviceId={} latitude={} longitude={}",
                LogCode.FAV_201_001, requestDto.getDeviceId(), requestDto.getLatitude(), requestDto.getLongitude());

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /** 디바이스별 즐겨찾기 목록 조회 */
    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<SmFavoriteLocationResponseDto>>> getSmFavoriteLocations(
            @RequestParam String deviceId) {
        log.info("[{}] favorite.controller.list.request endpoint=/api/sidemenu/locations deviceId={}",
                LogCode.FAV_REQ_002, deviceId);

        List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationService.getSmFavoriteLocations(deviceId);
        ApiResponse<List<SmFavoriteLocationResponseDto>> apiResponse =
                ApiResponse.success("즐겨찾기 목록을 성공적으로 조회했습니다.", locations);
        log.info("[{}] favorite.controller.list.success endpoint=/api/sidemenu/locations deviceId={} count={}",
                LogCode.FAV_200_001, deviceId, locations.size());
        return ResponseEntity.ok(apiResponse);
    }

    /** 즐겨찾기 위치 삭제 */
    @DeleteMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> deleteSmFavoriteLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId) {
        log.info("[{}] favorite.controller.delete.request endpoint=/api/sidemenu/locations deviceId={} latitude={} longitude={}",
                LogCode.FAV_REQ_003, deviceId, latitude, longitude);

        String deletedAddressName = smFavoriteLocationService.deleteSmFavoriteLocation(latitude, longitude, deviceId);
        String successMessage = deletedAddressName + "을(를) 즐겨찾기에서 삭제했습니다.";
        ApiResponse<Void> apiResponse = ApiResponse.success(successMessage, null);
        log.info("[{}] favorite.controller.delete.success endpoint=/api/sidemenu/locations deviceId={} latitude={} longitude={}",
                LogCode.FAV_200_002, deviceId, latitude, longitude);
        return ResponseEntity.ok(apiResponse);
    }

    /** 즐겨찾기 정렬 순서 변경 */
    @PatchMapping("/locations/sort-order")
    public ResponseEntity<ApiResponse<Void>> updateSmFavoriteLocationSortOrder(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId,
            @RequestParam Integer sortOrder) {
        log.info("[{}] favorite.controller.sort.request endpoint=/api/sidemenu/locations/sort-order deviceId={} latitude={} longitude={} sortOrder={}",
                LogCode.FAV_REQ_004, deviceId, latitude, longitude, sortOrder);

        smFavoriteLocationService.updateSmFavoriteLocationSortOrder(latitude, longitude, deviceId, sortOrder);
        ApiResponse<Void> apiResponse = ApiResponse.success("정렬 순서가 성공적으로 변경되었습니다.", null);
        log.info("[{}] favorite.controller.sort.success endpoint=/api/sidemenu/locations/sort-order deviceId={} latitude={} longitude={} sortOrder={}",
                LogCode.FAV_200_002, deviceId, latitude, longitude, sortOrder);
        return ResponseEntity.ok(apiResponse);
    }

    /** 즐겨찾기 중복 여부 확인 */
    @GetMapping("/locations/check-duplicate")
    public ResponseEntity<ApiResponse<Boolean>> checkDuplicateLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String deviceId) {
        log.info("[{}] favorite.controller.duplicate.request endpoint=/api/sidemenu/locations/check-duplicate deviceId={} latitude={} longitude={}",
                LogCode.FAV_REQ_005, deviceId, latitude, longitude);

        boolean isDuplicate = smFavoriteLocationService.checkDuplicateLocation(latitude, longitude, deviceId);
        ApiResponse<Boolean> apiResponse = ApiResponse.success("즐겨찾기 중복 여부를 조회했습니다.", isDuplicate);
        log.info("[{}] favorite.controller.duplicate.success endpoint=/api/sidemenu/locations/check-duplicate deviceId={} duplicate={}",
                LogCode.FAV_200_003, deviceId, isDuplicate);
        return ResponseEntity.ok(apiResponse);
    }
}
