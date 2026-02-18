package com.server.KH_StudyProjects_WeatherServer.domain.weather.service;

import com.server.KH_StudyProjects_WeatherServer.domain.weather.dto.WeatherRequestDto;
import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client.OpenMeteoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/** 날씨 도메인 서비스 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    private final OpenMeteoClient openMeteoClient;

    /** 외부 API에서 날씨 정보를 조회해 응답한다. */
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWeather(WeatherRequestDto requestDto) {
        log.info("날씨 조회 요청: {}", requestDto);
        Optional<Map<String, Object>> response = openMeteoClient.fetchWeather(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam()
        );
        if (response.isEmpty()) {
            ApiResponse<Map<String, Object>> apiResponse = ApiResponse.error("날씨 조회 결과가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }

        ApiResponse<Map<String, Object>> apiResponse =
                ApiResponse.success("날씨 정보를 성공적으로 조회했습니다.", response.get());
        return ResponseEntity.ok(apiResponse);
    }
}
