package com.server.KH_StudyProjects_WeatherServer.service;

import com.server.KH_StudyProjects_WeatherServer.component.OpenApiManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class OpenApiService {

    private final OpenApiManager openApiManager;

    @Autowired
    public OpenApiService(OpenApiManager openApiManager) {
        this.openApiManager = openApiManager;
    }

    public ResponseEntity<?> executeApi(Map<String, Object> params,String flag) throws Exception {
        log.info(">> ActionService executeAction : {}", params);

        Optional<Map<String, Object>> oData = openApiManager.executeContext(params,flag);
        return oData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
