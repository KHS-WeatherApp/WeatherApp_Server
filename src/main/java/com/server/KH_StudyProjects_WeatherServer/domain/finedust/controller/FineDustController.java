package com.server.KH_StudyProjects_WeatherServer.domain.finedust.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.finedust.dto.FineDustRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.finedust.service.FineDustService;
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
public class FineDustController {
    private final FineDustService fineDustService;

    /** 대기질 정보 조회 (날씨 화면에서 사용) */
    @PostMapping("/api/airPollution")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFineDust(@Valid @RequestBody FineDustRequestDto requestDto) {
        return fineDustService.getFineDust(requestDto);
    }
}
