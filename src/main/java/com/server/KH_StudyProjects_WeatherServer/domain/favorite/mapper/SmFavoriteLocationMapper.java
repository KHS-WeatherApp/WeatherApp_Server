package com.server.KH_StudyProjects_WeatherServer.domain.favorite.mapper;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 사이드메뉴 즐겨찾기 MyBatis Mapper */
@Mapper
public interface SmFavoriteLocationMapper {

    /** 즐겨찾기 위치 추가 */
    int insertFavoriteLocation(SmFavoriteLocationRequestDto requestDto);

    /** 디바이스별 즐겨찾기 목록 조회 */
    List<SmFavoriteLocationResponseDto> selectFavoriteLocationsByUserId(@Param("deviceId") String deviceId);

    /** 동일 좌표 즐겨찾기 존재 여부 확인 */
    boolean existsByLatitudeAndLongitude(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("deviceId") String deviceId
    );

    /** 디바이스별 즐겨찾기 개수 조회 */
    int countFavoriteLocationsByDeviceId(@Param("deviceId") String deviceId);

    /** 즐겨찾기 위치 삭제 */
    int deleteFavoriteLocation(
            @Param("deviceId") String deviceId,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude
    );

    /** 즐겨찾기 정렬 순서 변경 */
    int updateFavoriteLocationSortOrder(
            @Param("deviceId") String deviceId,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("sortOrder") Integer sortOrder
    );
}
