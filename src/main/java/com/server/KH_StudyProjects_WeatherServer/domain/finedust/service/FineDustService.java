package com.server.KH_StudyProjects_WeatherServer.domain.finedust.service;

import com.server.KH_StudyProjects_WeatherServer.domain.finedust.dto.FineDustRequestDto;
import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client.OpenMeteoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFineDust(FineDustRequestDto requestDto) {
        log.info("대기질 조회 요청: {}", requestDto);
        Optional<Map<String, Object>> response = openMeteoClient.fetchFineDust(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getQueryParam()
        );
        if (response.isEmpty()) {
            ApiResponse<Map<String, Object>> apiResponse = ApiResponse.error("대기질 조회 결과가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }

        ApiResponse<Map<String, Object>> apiResponse =
                ApiResponse.success("대기질 정보를 성공적으로 조회했습니다.", response.get());
        return ResponseEntity.ok(apiResponse);
    }
}
