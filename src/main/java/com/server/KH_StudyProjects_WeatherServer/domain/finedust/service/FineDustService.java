package com.server.KH_StudyProjects_WeatherServer.domain.finedust.service;

import com.server.KH_StudyProjects_WeatherServer.domain.finedust.dto.FineDustRequestDto;
import com.server.KH_StudyProjects_WeatherServer.global.logging.LogCode;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client.OpenMeteoClient;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.dto.OpenMeteoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/** 대기질 도메인 서비스 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FineDustService {
    private final OpenMeteoClient openMeteoClient;

    /** 외부 API에서 대기질 정보를 조회해 응답한다. */
    public ResponseEntity<Map<String, Object>> getFineDust(FineDustRequestDto requestDto) {
        log.info("[{}] finedust.request endpoint=/api/airPollution latitude={} longitude={} queryLength={}",
                LogCode.FDT_REQ_001,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam() != null ? requestDto.getQueryParam().length() : 0);

        Optional<OpenMeteoApiResponseDto> response = openMeteoClient.fetchFineDust(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam()
        );
        if (response.isEmpty()) {
            log.warn("[{}] finedust.not-found endpoint=/api/airPollution latitude={} longitude={}",
                    LogCode.FDT_404_001, requestDto.getLatitude(), requestDto.getLongitude());
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> payload = response.get().toApiPayload();
        log.info("[{}] finedust.success endpoint=/api/airPollution latitude={} longitude={} dataKeys={}",
                LogCode.FDT_200_001,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                payload.keySet());
        return ResponseEntity.ok(payload);
    }
}
