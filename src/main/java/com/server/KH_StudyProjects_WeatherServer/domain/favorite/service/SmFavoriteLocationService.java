package com.server.KH_StudyProjects_WeatherServer.domain.favorite.service;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.DuplicateLocationException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.LocationNotFoundException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.mapper.SmFavoriteLocationMapper;
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
        log.info("즐겨찾기 추가 요청: {}", requestDto);

        if (smFavoriteLocationMapper.existsByLatitudeAndLongitude(
                requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getDeviceId())) {
            throw new DuplicateLocationException("이미 즐겨찾기에 등록된 지역입니다.");
        }

        int result = smFavoriteLocationMapper.insertFavoriteLocation(requestDto);
        if (result > 0) {
            log.info("즐겨찾기 저장 완료");
            return getSmFavoriteLocationByCoordinates(
                    requestDto.getLatitude(),
                    requestDto.getLongitude(),
                    requestDto.getDeviceId()
            );
        }

        throw new RuntimeException("즐겨찾기 저장에 실패했습니다.");
    }

    /** 디바이스별 즐겨찾기 목록 조회 */
    @Transactional(readOnly = true)
    public List<SmFavoriteLocationResponseDto> getSmFavoriteLocations(String deviceId) {
        log.info("즐겨찾기 목록 조회: deviceId={}", deviceId);
        return smFavoriteLocationMapper.selectFavoriteLocationsByUserId(deviceId);
    }

    /** 즐겨찾기 위치 삭제 */
    public String deleteSmFavoriteLocation(Double latitude, Double longitude, String deviceId) {
        log.info("즐겨찾기 삭제 요청: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);

        SmFavoriteLocationResponseDto locationToDelete = getSmFavoriteLocationByCoordinates(latitude, longitude, deviceId);
        if (locationToDelete == null) {
            throw new LocationNotFoundException("삭제할 즐겨찾기 위치를 찾을 수 없습니다.");
        }

        String addressName = locationToDelete.getAddressName();
        int result = smFavoriteLocationMapper.deleteFavoriteLocation(deviceId, latitude, longitude);
        if (result > 0) {
            log.info("즐겨찾기 삭제 완료: lat={}, lng={}, addressName={}", latitude, longitude, addressName);
            return addressName;
        }

        throw new RuntimeException("즐겨찾기 삭제에 실패했습니다.");
    }

    /** 중복 위치 여부 확인 */
    @Transactional(readOnly = true)
    public boolean checkDuplicateLocation(Double latitude, Double longitude, String deviceId) {
        log.info("중복 위치 확인: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
        return smFavoriteLocationMapper.existsByLatitudeAndLongitude(latitude, longitude, deviceId);
    }

    /** 즐겨찾기 정렬 순서 변경 */
    public void updateSmFavoriteLocationSortOrder(Double latitude, Double longitude, String deviceId, Integer sortOrder) {
        log.info("정렬 순서 변경 요청: lat={}, lng={}, deviceId={}, sortOrder={}",
                latitude, longitude, deviceId, sortOrder);

        int result = smFavoriteLocationMapper.updateFavoriteLocationSortOrder(deviceId, latitude, longitude, sortOrder);
        if (result > 0) {
            log.info("정렬 순서 변경 완료: lat={}, lng={}, sortOrder={}", latitude, longitude, sortOrder);
            return;
        }

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
