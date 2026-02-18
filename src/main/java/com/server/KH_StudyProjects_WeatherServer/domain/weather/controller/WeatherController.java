package com.server.KH_StudyProjects_WeatherServer.domain.weather.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.weather.dto.WeatherRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.weather.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherController {
    private final WeatherService weatherService;

    /** 날씨 정보 조회 */
    @PostMapping("/api/weather")
    public ResponseEntity<?> getWeather(@Valid @RequestBody WeatherRequestDto requestDto) {
        return weatherService.getWeather(requestDto);
    }
}
