package com.server.KH_StudyProjects_WeatherServer.domain.favorite.service;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.DuplicateLocationException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.LocationNotFoundException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.mapper.SmFavoriteLocationMapper;
import com.server.KH_StudyProjects_WeatherServer.global.logging.LogCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 사이드메뉴 즐겨찾기 서비스 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SmFavoriteLocationService {

    private final SmFavoriteLocationMapper smFavoriteLocationMapper;

    /** 즐겨찾기 위치 추가 */
    public SmFavoriteLocationResponseDto addSmFavoriteLocation(SmFavoriteLocationRequestDto requestDto) {
        log.info("[{}] favorite.service.add.request deviceId={} latitude={} longitude={}",
                LogCode.FAV_REQ_001,
                requestDto.getDeviceId(),
                requestDto.getLatitude(),
                requestDto.getLongitude());

        if (smFavoriteLocationMapper.existsByLatitudeAndLongitude(
                requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getDeviceId())) {
            log.warn("[{}] favorite.service.add.duplicate deviceId={} latitude={} longitude={}",
                    LogCode.FAV_409_001,
                    requestDto.getDeviceId(),
                    requestDto.getLatitude(),
                    requestDto.getLongitude());
            throw new DuplicateLocationException("이미 즐겨찾기에 등록된 지역입니다.");
        }

        int result = smFavoriteLocationMapper.insertFavoriteLocation(requestDto);
        if (result > 0) {
            log.info("[{}] favorite.service.add.success deviceId={} latitude={} longitude={} insertCount={}",
                    LogCode.FAV_201_001,
                    requestDto.getDeviceId(),
                    requestDto.getLatitude(),
                    requestDto.getLongitude(),
                    result);
            return getSmFavoriteLocationByCoordinates(
                    requestDto.getLatitude(),
                    requestDto.getLongitude(),
                    requestDto.getDeviceId()
            );
        }

        log.error("[{}] favorite.service.add.fail deviceId={} latitude={} longitude={} insertCount={}",
                LogCode.FAV_500_001,
                requestDto.getDeviceId(),
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                result);
        throw new RuntimeException("즐겨찾기 저장에 실패했습니다.");
    }

    /** 디바이스별 즐겨찾기 목록 조회 */
    @Transactional(readOnly = true)
    public List<SmFavoriteLocationResponseDto> getSmFavoriteLocations(String deviceId) {
        log.info("[{}] favorite.service.list.request deviceId={}", LogCode.FAV_REQ_002, deviceId);
        List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationMapper.selectFavoriteLocationsByUserId(deviceId);
        log.info("[{}] favorite.service.list.success deviceId={} count={}",
                LogCode.FAV_200_001, deviceId, locations.size());
        return locations;
    }

    /** 즐겨찾기 위치 삭제 */
    public String deleteSmFavoriteLocation(Double latitude, Double longitude, String deviceId) {
        log.info("[{}] favorite.service.delete.request deviceId={} latitude={} longitude={}",
                LogCode.FAV_REQ_003, deviceId, latitude, longitude);

        SmFavoriteLocationResponseDto locationToDelete = getSmFavoriteLocationByCoordinates(latitude, longitude, deviceId);
        if (locationToDelete == null) {
            log.warn("[{}] favorite.service.delete.not-found deviceId={} latitude={} longitude={}",
                    LogCode.FAV_404_001, deviceId, latitude, longitude);
            throw new LocationNotFoundException("삭제할 즐겨찾기 위치를 찾을 수 없습니다.");
        }

        String addressName = locationToDelete.getAddressName();
        int result = smFavoriteLocationMapper.deleteFavoriteLocation(deviceId, latitude, longitude);
        if (result > 0) {
            log.info("[{}] favorite.service.delete.success deviceId={} latitude={} longitude={} addressName={} deleteCount={}",
                    LogCode.FAV_200_002, deviceId, latitude, longitude, addressName, result);
            return addressName;
        }

        log.error("[{}] favorite.service.delete.fail deviceId={} latitude={} longitude={} deleteCount={}",
                LogCode.FAV_500_002, deviceId, latitude, longitude, result);
        throw new RuntimeException("즐겨찾기 삭제에 실패했습니다.");
    }

    /** 중복 위치 여부 확인 */
    @Transactional(readOnly = true)
    public boolean checkDuplicateLocation(Double latitude, Double longitude, String deviceId) {
        log.info("[{}] favorite.service.duplicate.request deviceId={} latitude={} longitude={}",
                LogCode.FAV_REQ_005, deviceId, latitude, longitude);
        boolean duplicate = smFavoriteLocationMapper.existsByLatitudeAndLongitude(latitude, longitude, deviceId);
        log.info("[{}] favorite.service.duplicate.success deviceId={} duplicate={}",
                LogCode.FAV_200_003, deviceId, duplicate);
        return duplicate;
    }

    /** 즐겨찾기 정렬 순서 변경 */
    public void updateSmFavoriteLocationSortOrder(Double latitude, Double longitude, String deviceId, Integer sortOrder) {
        log.info("[{}] favorite.service.sort.request deviceId={} latitude={} longitude={} sortOrder={}",
                LogCode.FAV_REQ_004, deviceId, latitude, longitude, sortOrder);

        int result = smFavoriteLocationMapper.updateFavoriteLocationSortOrder(deviceId, latitude, longitude, sortOrder);
        if (result > 0) {
            log.info("[{}] favorite.service.sort.success deviceId={} latitude={} longitude={} sortOrder={} updateCount={}",
                    LogCode.FAV_200_002, deviceId, latitude, longitude, sortOrder, result);
            return;
        }

        log.warn("[{}] favorite.service.sort.not-found deviceId={} latitude={} longitude={} sortOrder={}",
                LogCode.FAV_404_001, deviceId, latitude, longitude, sortOrder);
        throw new LocationNotFoundException("정렬 순서를 변경할 즐겨찾기 위치를 찾을 수 없습니다.");
    }

    /** 좌표 기준 즐겨찾기 단건 조회 */
    @Transactional(readOnly = true)
    private SmFavoriteLocationResponseDto getSmFavoriteLocationByCoordinates(Double latitude, Double longitude, String deviceId) {
        List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationMapper.selectFavoriteLocationsByUserId(deviceId);
        return locations.stream()
                .filter(loc -> loc.getLatitude().equals(latitude) && loc.getLongitude().equals(longitude))
                .findFirst()
                .orElse(null);
    }
}
