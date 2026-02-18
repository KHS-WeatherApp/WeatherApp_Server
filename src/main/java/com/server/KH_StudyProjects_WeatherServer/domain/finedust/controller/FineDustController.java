package com.server.KH_StudyProjects_WeatherServer.domain.finedust.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.finedust.dto.FineDustRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.finedust.service.FineDustService;
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
public class FineDustController {
    private final FineDustService fineDustService;

    /** 추가 날씨 정보 - 미세먼지,초미세먼지 */
    @PostMapping("/api/airPollution")
    public ResponseEntity<?> getFineDust(@Valid @RequestBody FineDustRequestDto requestDto) {
        return fineDustService.getFineDust(requestDto);
    }
}
