package com.server.KH_StudyProjects_WeatherServer.domain.weather.service;

import com.server.KH_StudyProjects_WeatherServer.domain.weather.dto.WeatherRequestDto;
import com.server.KH_StudyProjects_WeatherServer.global.logging.LogCode;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client.OpenMeteoClient;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.dto.OpenMeteoApiResponseDto;
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
    public ResponseEntity<Map<String, Object>> getWeather(WeatherRequestDto requestDto) {
        log.info("[{}] weather.request endpoint=/api/weather latitude={} longitude={} queryLength={}",
                LogCode.WTH_REQ_001,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam() != null ? requestDto.getQueryParam().length() : 0);

        Optional<OpenMeteoApiResponseDto> response = openMeteoClient.fetchWeather(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam()
        );
        if (response.isEmpty()) {
            log.warn("[{}] weather.not-found endpoint=/api/weather latitude={} longitude={}",
                    LogCode.WTH_404_001, requestDto.getLatitude(), requestDto.getLongitude());
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> payload = response.get().toApiPayload();
        log.info("[{}] weather.success endpoint=/api/weather latitude={} longitude={} dataKeys={}",
                LogCode.WTH_200_001,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                payload.keySet());
        return ResponseEntity.ok(payload);
    }
}
