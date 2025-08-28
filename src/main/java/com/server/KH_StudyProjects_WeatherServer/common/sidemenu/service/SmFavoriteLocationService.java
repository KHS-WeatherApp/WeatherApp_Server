package com.server.KH_StudyProjects_WeatherServer.common.sidemenu.service;

import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationResponseDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.mapper.SmFavoriteLocationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사이드메뉴 즐겨찾기 위치 서비스
 * 
 * Android 앱에서 사이드메뉴 검색 결과를 즐겨찾기로 저장하고 관리하는 비즈니스 로직을 담당
 * COM_FAVORITE_LOCATIONS 테이블과 상호작용하여 CRUD 작업 수행
 * 
 * 주요 기능:
 * - 즐겨찾기 위치 추가 (중복 체크 포함)
 * - 사용자별 즐겨찾기 목록 조회
 * - 즐겨찾기 위치 삭제 (권한 체크 포함)
 * - 중복 위치 체크
 * - 위도/경도 기반 위치 조회
 * 
 * @author 김효동
 * @since 2025-08-27
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SmFavoriteLocationService {
    
    private final SmFavoriteLocationMapper smFavoriteLocationMapper;
    
    /**
     * 사이드메뉴 즐겨찾기 위치 추가
     * 
     * Android 앱에서 사이드메뉴 검색 결과를 클릭하여 즐겨찾기로 저장할 때 호출
     * 
     * 처리 과정:
     * 1. 위도/경도 기반 중복 위치 체크
     * 2. 중복이 아닌 경우 DB에 저장
     * 3. 저장 성공 시 저장된 데이터를 조회하여 반환
     * 
     * @param requestDto 즐겨찾기 추가 요청 데이터 (주소명, 위도, 경도, 지역정보, 디바이스ID)
     * @return SmFavoriteLocationResponseDto 저장된 즐겨찾기 위치 정보
     * @throws RuntimeException 중복 위치인 경우 또는 저장 실패 시
     */
    public SmFavoriteLocationResponseDto addSmFavoriteLocation(SmFavoriteLocationRequestDto requestDto) {
        log.info("사이드메뉴 즐겨찾기 위치 추가 요청: {}", requestDto);
        
        // 1단계: 중복 체크 (동일한 위도/경도 + 디바이스ID 조합이 이미 존재하는지)
        if (smFavoriteLocationMapper.existsByLatitudeAndLongitude(
                requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getDeviceId())) {
            throw new RuntimeException("이미 사이드메뉴 즐겨찾기에 등록된 위치입니다.");
        }
        
        // 2단계: DB에 저장
        int result = smFavoriteLocationMapper.insertFavoriteLocation(requestDto);
        if (result > 0) {
            log.info("사이드메뉴 즐겨찾기 위치 저장 완료");
            // 3단계: 저장된 데이터 조회하여 반환 (SEQ_NO 포함된 완전한 정보)
            return getSmFavoriteLocationByCoordinates(requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getDeviceId());
        } else {
            throw new RuntimeException("사이드메뉴 즐겨찾기 위치 저장에 실패했습니다.");
        }
    }
    
    /**
     * 사용자의 사이드메뉴 즐겨찾기 목록 조회
     * 
     * Android 앱에서 즐겨찾기 목록을 표시할 때 호출
     * 
     * 정렬 순서:
     * 1. SORT_ORDER ASC (사용자가 지정한 순서)
     * 2. CREATED_AT DESC (생성 시간 역순, SORT_ORDER가 NULL인 경우)
     * 
     * @param deviceId 디바이스 고유 식별자
     * @return List<SmFavoriteLocationResponseDto> 해당 디바이스의 즐겨찾기 위치 목록
     */
    @Transactional(readOnly = true)
    public List<SmFavoriteLocationResponseDto> getSmFavoriteLocations(String deviceId) {
        log.info("사용자 사이드메뉴 즐겨찾기 목록 조회: deviceId={}", deviceId);
        
        return smFavoriteLocationMapper.selectFavoriteLocationsByUserId(deviceId);
    }
    
    /**
     * 사이드메뉴 즐겨찾기 위치 삭제
     * 
     * Android 앱에서 즐겨찾기 항목을 삭제할 때 호출
     * 
     * 보안 처리:
     * 1. 디바이스ID 기반 권한 체크 (본인의 즐겨찾기만 삭제 가능)
     * 2. 위도/경도로 정확한 위치 식별 후 삭제
     * 
     * @param latitude 삭제할 위치의 위도
     * @param longitude 삭제할 위치의 경도
     * @param deviceId 삭제 요청한 디바이스의 ID
     * @throws RuntimeException 위치를 찾을 수 없거나 권한이 없는 경우
     */
    public void deleteSmFavoriteLocation(Double latitude, Double longitude, String deviceId) {
        log.info("사이드메뉴 즐겨찾기 위치 삭제: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
        
        // 1단계: 실제 삭제 수행 (디바이스ID + 위도/경도로 정확한 위치 식별)
        int result = smFavoriteLocationMapper.deleteFavoriteLocation(deviceId, latitude, longitude);
        if (result > 0) {
            log.info("사이드메뉴 즐겨찾기 위치 삭제 완료: lat={}, lng={}", latitude, longitude);
        } else {
            throw new RuntimeException("사이드메뉴 즐겨찾기 위치 삭제에 실패했습니다.");
        }
    }
    

    
    /**
     * 중복 위치 체크
     * 
     * Android 앱에서 새로운 위치를 즐겨찾기로 추가하기 전에 중복 여부를 확인할 때 호출
     * 
     * 중복 판단 기준:
     * - 동일한 위도 + 동일한 경도 + 동일한 디바이스ID
     * - 정확한 위치 중복 방지 (약간의 오차도 허용하지 않음)
     * 
     * @param latitude 위도 (Double)
     * @param longitude 경도 (Double)
     * @param deviceId 디바이스 고유 식별자
     * @return boolean true: 중복 위치 존재, false: 중복 위치 없음
     */
    @Transactional(readOnly = true)
    public boolean checkDuplicateLocation(Double latitude, Double longitude, String deviceId) {
        log.info("중복 위치 체크: lat={}, lng={}, deviceId={}", latitude, longitude, deviceId);
        
        return smFavoriteLocationMapper.existsByLatitudeAndLongitude(latitude, longitude, deviceId);
    }
    
    /**
     * 사이드메뉴 즐겨찾기 위치 정렬 순서 변경
     * 
     * Android 앱에서 드래그앤드롭으로 즐겨찾기 순서를 변경할 때 호출
     * 
     * 보안 처리:
     * 1. 디바이스ID 기반 권한 체크 (본인의 즐겨찾기만 수정 가능)
     * 2. 위도/경도로 정확한 위치 식별 후 정렬 순서 변경
     * 
     * @param latitude 변경할 위치의 위도
     * @param longitude 변경할 위치의 경도
     * @param deviceId 수정 요청한 디바이스의 ID
     * @param sortOrder 새로운 정렬 순서
     * @throws RuntimeException 위치를 찾을 수 없거나 권한이 없는 경우
     */
    public void updateSmFavoriteLocationSortOrder(Double latitude, Double longitude, String deviceId, Integer sortOrder) {
        log.info("사이드메뉴 즐겨찾기 위치 정렬 순서 변경: lat={}, lng={}, deviceId={}, sortOrder={}", 
                latitude, longitude, deviceId, sortOrder);
        
        // 1단계: 실제 정렬 순서 변경 (디바이스ID + 위도/경도로 정확한 위치 식별)
        int result = smFavoriteLocationMapper.updateFavoriteLocationSortOrder(deviceId, latitude, longitude, sortOrder);
        if (result > 0) {
            log.info("사이드메뉴 즐겨찾기 위치 정렬 순서 변경 완료: lat={}, lng={}, sortOrder={}", 
                    latitude, longitude, sortOrder);
        } else {
            throw new RuntimeException("사이드메뉴 즐겨찾기 위치 정렬 순서 변경에 실패했습니다.");
        }
    }
    
    /**
     * 위도/경도로 사이드메뉴 즐겨찾기 위치 조회 (내부 메서드)
     * 
     * 즐겨찾기 추가 후 저장된 데이터를 조회하여 반환할 때 사용
     * 
     * 동작 방식:
     * 1. 해당 디바이스의 모든 즐겨찾기 목록 조회
     * 2. Stream API를 사용하여 위도/경도가 일치하는 항목 필터링
     * 3. 첫 번째 일치 항목 반환 (없으면 null)
     * 
     * @param latitude 위도 (Double)
     * @param longitude 경도 (Double)
     * @param deviceId 디바이스 고유 식별자
     * @return SmFavoriteLocationResponseDto 일치하는 위치 정보 또는 null
     */
    @Transactional(readOnly = true)
    private SmFavoriteLocationResponseDto getSmFavoriteLocationByCoordinates(Double latitude, Double longitude, String deviceId) {
        // 위도/경도로 해당 위치 조회
        List<SmFavoriteLocationResponseDto> locations = smFavoriteLocationMapper.selectFavoriteLocationsByUserId(deviceId);
        return locations.stream()
                .filter(loc -> loc.getLatitude().equals(latitude) && loc.getLongitude().equals(longitude))
                .findFirst()
                .orElse(null);
    }
}
