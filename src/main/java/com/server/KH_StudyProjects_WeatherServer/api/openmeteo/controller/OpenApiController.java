package com.server.KH_StudyProjects_WeatherServer.api.openmeteo.controller;

import com.server.KH_StudyProjects_WeatherServer.api.openmeteo.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OpenApiController {
    private final OpenApiService openApiService;

    @SuppressWarnings("finally")
    @PostMapping("/api/weather")
    public ResponseEntity<?> executeModule(@RequestBody Map<String, Object> params) {
        log.info(">>>> mathod  /api/executeModule param : {}", params);
        ResponseEntity<?> retMap = null;
        try {
            retMap = openApiService.executeApi(params, "weather"); //2025.04.13 이수연 flag로 분기처리
        } catch (Exception e) {
            log.error(">> ActionController executeModule e : {}", e.toString());
        } finally
        {
            return Optional.ofNullable(retMap).orElse(ResponseEntity.notFound().build());

        }
    }

    //2025.04.13 이수연 추가
    @SuppressWarnings("finally")
    @PostMapping("/api/airPollution")
    public ResponseEntity<?> executeModuleAirPollution(@RequestBody Map<String, Object> params) {
        log.info(">>>> method  /api/executeModuleAirPollution param : {}", params);
        ResponseEntity<?> retMap = null;
        try {
            retMap = openApiService.executeApi(params,"airPollution");//2025.04.13 이수연 flag로 분기처리
        } catch (Exception e) {
            log.error(">> ActionController executeModule e : {}", e.toString());
        } finally
        {
            return Optional.ofNullable(retMap).orElse(ResponseEntity.notFound().build());

        }
    }

}
