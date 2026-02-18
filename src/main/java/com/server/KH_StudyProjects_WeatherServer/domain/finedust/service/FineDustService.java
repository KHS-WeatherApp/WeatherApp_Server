package com.server.KH_StudyProjects_WeatherServer.domain.finedust.service;

import com.server.KH_StudyProjects_WeatherServer.domain.finedust.dto.FineDustRequestDto;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client.OpenMeteoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/** 대기질(미세먼지) 도메인 서비스 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FineDustService {
    private final OpenMeteoClient openMeteoClient;

    /** 외부 API에서 대기질 정보를 조회해 응답한다. */
    public ResponseEntity<?> getFineDust(FineDustRequestDto requestDto) {
        log.info("Fine dust request: {}", requestDto);
        Optional<Map<String, Object>> response = openMeteoClient.fetchFineDust(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam()
        );
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
