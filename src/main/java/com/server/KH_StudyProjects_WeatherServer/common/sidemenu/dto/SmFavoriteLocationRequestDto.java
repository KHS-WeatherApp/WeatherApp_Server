package com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사이드메뉴 즐겨찾기 위치 추가 요청 DTO
 * 
 * Android 앱에서 사이드메뉴 검색 결과를 클릭하여 즐겨찾기로 저장할 때 사용
 * COM_FAVORITE_LOCATIONS 테이블에 저장될 데이터 구조와 일치
 * 
 * @author 김효동
 * @since 2025-08-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmFavoriteLocationRequestDto {
    
    private String addressName;     // 주소명
    private Double latitude;        // 위도
    private Double longitude;       // 경도
    private String region1DepthName; // 시/도
    private String region2DepthName; // 구/군
    private String region3DepthName; // 동/읍/면
    private String region3DepthHName; // 동/읍/면 한글명
    private String deviceId;         // 디바이스식별번호
}
