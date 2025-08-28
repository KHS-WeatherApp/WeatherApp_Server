package com.server.KH_StudyProjects_WeatherServer.common.sidemenu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사이드메뉴 즐겨찾기 위치 응답 DTO
 * 
 * COM_FAVORITE_LOCATIONS 테이블에서 조회된 데이터를 Android 앱으로 전달할 때 사용
 * 즐겨찾기 목록 조회, 상세 조회, 추가 후 반환 등에 활용
 * 
 * @author 김효동
 * @since 2025-08-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmFavoriteLocationResponseDto {
    
    private Long seqNo;
    private String deviceId;        // 디바이스식별번호
    private Double latitude;        // 위도
    private Double longitude;       // 경도
    private String addressName;     // 주소명
    private String region1DepthName; // 시/도
    private String region2DepthName; // 구/군
    private String region3DepthName; // 동/읍/면
    private Integer sortOrder;       // 정렬순서
    private LocalDateTime createdAt;
}
