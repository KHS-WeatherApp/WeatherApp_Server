package com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/** Open-Meteo 외부 API 호출 클라이언트 */
@Component
@Slf4j
public class OpenMeteoClient {

    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1";
    private static final String FINE_DUST_BASE_URL = "https://air-quality-api.open-meteo.com/v1";
    private final RestTemplate restTemplate = new RestTemplate();

    /** 날씨 API를 호출한다. */
    public Optional<Map<String, Object>> fetchWeather(Double latitude, Double longitude, String queryParam) {
        return callApi(WEATHER_BASE_URL, "/forecast", latitude, longitude, queryParam);
    }

    /** 대기질 API를 호출한다. */
    public Optional<Map<String, Object>> fetchFineDust(Double latitude, Double longitude, String queryParam) {
        return callApi(FINE_DUST_BASE_URL, "/air-quality", latitude, longitude, queryParam);
    }

    /** 공통 GET 호출을 수행하고 응답 바디를 반환한다. */
    private Optional<Map<String, Object>> callApi(
            String baseUrl,
            String path,
            Double latitude,
            Double longitude,
            String queryParam
    ) {
        try {
            String apiUri = buildUrl(baseUrl, path, latitude, longitude, queryParam);
            ResponseEntity<Map> response = restTemplate.getForEntity(new URI(apiUri), Map.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error occurred while calling the API: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /** 위도/경도 및 queryParam을 조합해 호출 URL을 생성한다. */
    private String buildUrl(String baseUrl, String path, Double latitude, Double longitude, String queryParam) {
        return new StringBuilder()
                .append(baseUrl)
                .append(path)
                .append("?latitude=")
                .append(latitude)
                .append("&longitude=")
                .append(longitude)
                .append("&")
                .append(queryParam)
                .toString();
    }
}
