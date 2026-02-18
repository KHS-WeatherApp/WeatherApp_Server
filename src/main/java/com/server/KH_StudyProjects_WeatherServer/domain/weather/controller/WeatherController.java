package com.server.KH_StudyProjects_WeatherServer.domain.weather.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.weather.dto.WeatherRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.weather.service.WeatherService;
import com.server.KH_StudyProjects_WeatherServer.global.logging.LogCode;
import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherController {
    private final WeatherService weatherService;

    /** 날씨 정보 조회 */
    @PostMapping("/api/weather")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWeather(@Valid @RequestBody WeatherRequestDto requestDto) {
        log.info("[{}] weather.controller.request endpoint=/api/weather latitude={} longitude={} queryLength={}",
                LogCode.WTH_REQ_001,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam() != null ? requestDto.getQueryParam().length() : 0);
        return weatherService.getWeather(requestDto);
    }
}
