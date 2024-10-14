package com.server.KH_StudyProjects_WeatherServer.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class OpenApiManager {

    private final String BASE_URL = "https://api.open-meteo.com/v1";

    // RestTemplate을 사용하여 API 호출 메서드
    public Optional<Map<String, Object>> executeContext(Map<String, Object> params) throws URISyntaxException, UnsupportedEncodingException {
        String apiUri = makeUrl(params); // 동적으로 URL 생성
        RestTemplate restTemplate = new RestTemplate();

        try {
            // API 호출
            ResponseEntity<Map> response = restTemplate.getForEntity(new URI(apiUri), Map.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error occurred while calling the API: {}", e.getMessage());
            return Optional.empty();
        }
    }

    // BASE_URL에 쿼리 파라미터 추가
    private String makeUrl(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("/forecast"); // 원하는 경로 추가

        // Query Parameter 설정 (params에서 동적으로 추가)
        url.append("?latitude=").append(params.get("latitude"))
                .append("&longitude=").append(params.get("longitude"))
                .append("&").append(params.get("queryParam"));

        return url.toString();
    }
}
