package com.server.KH_StudyProjects_WeatherServer.common.sidemenu.mapper;

import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto.SmFavoriteLocationResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
     * 사이드메뉴 즐겨찾기 위치 MyBatis Mapper 인터페이스
     * 
     * COM_FAVORITE_LOCATIONS 테이블과의 데이터베이스 상호작용을 담당
     * MyBatis XML 파일(SmFavoriteLocationMapper.xml)과 연동하여 SQL 쿼리 실행
     * 
     * 주요 기능:
     * - 즐겨찾기 위치 추가 (INSERT)
     * - 사용자별 즐겨찾기 목록 조회 (SELECT)
     * - 중복 위치 체크 (SELECT COUNT)
     * - 즐겨찾기 위치 삭제 (DELETE)
     * - 즐겨찾기 위치 정렬 순서 수정 (UPDATE)
     * 
     * @author 김효동
     * @since 2025-08-27
     */
@Mapper
public interface SmFavoriteLocationMapper {
    
    /**
     * 즐겨찾기 위치 추가
     */
    int insertFavoriteLocation(SmFavoriteLocationRequestDto requestDto);
    
    /**
     * 사용자별 즐겨찾기 목록 조회
     */
    List<SmFavoriteLocationResponseDto> selectFavoriteLocationsByUserId(@Param("deviceId") String deviceId);
    
        /**
     * 중복 위치 체크
     */
    boolean existsByLatitudeAndLongitude(@Param("latitude") Double latitude,
                                       @Param("longitude") Double longitude,
                                       @Param("deviceId") String deviceId);
    
    /**
     * 디바이스별 즐겨찾기 개수 조회
     */
    int countFavoriteLocationsByDeviceId(@Param("deviceId") String deviceId);
    
    /**
     * 즐겨찾기 위치 삭제
     */
    int deleteFavoriteLocation(@Param("deviceId") String deviceId,
                             @Param("latitude") Double latitude,
                             @Param("longitude") Double longitude);
    
    /**
     * 즐겨찾기 위치 정렬 순서 수정
     */
    int updateFavoriteLocationSortOrder(@Param("deviceId") String deviceId,
                                      @Param("latitude") Double latitude,
                                      @Param("longitude") Double longitude,
                                      @Param("sortOrder") Integer sortOrder);
}
