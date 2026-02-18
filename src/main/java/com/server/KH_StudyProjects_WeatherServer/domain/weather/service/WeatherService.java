package com.server.KH_StudyProjects_WeatherServer.domain.weather.service;

import com.server.KH_StudyProjects_WeatherServer.domain.weather.dto.WeatherRequestDto;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client.OpenMeteoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> getWeather(WeatherRequestDto requestDto) {
        log.info("Weather request: {}", requestDto);
        Optional<Map<String, Object>> response = openMeteoClient.fetchWeather(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam()
        );
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
