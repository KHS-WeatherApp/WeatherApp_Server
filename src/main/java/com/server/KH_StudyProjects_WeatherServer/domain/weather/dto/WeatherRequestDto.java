package com.server.KH_StudyProjects_WeatherServer.domain.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 날씨 조회 요청 DTO */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRequestDto {
    /** 위도 */
    @NotNull
    private Double latitude;

    /** 경도 */
    @NotNull
    private Double longitude;

    /** Open-Meteo 조회 쿼리 문자열 */
    @NotBlank
    private String queryParam;
}
