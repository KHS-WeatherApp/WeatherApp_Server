package com.server.KH_StudyProjects_WeatherServer.controller;

import com.server.KH_StudyProjects_WeatherServer.service.OpenApiService;
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
            retMap = openApiService.executeApi(params);
        } catch (Exception e) {
            log.error(">> ActionController executeModule e : {}", e.toString());
        } finally
        {
            return Optional.ofNullable(retMap).orElse(ResponseEntity.notFound().build());

        }
    }
}
