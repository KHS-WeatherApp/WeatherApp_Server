package com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client;

import com.server.KH_StudyProjects_WeatherServer.global.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Optional;

/** Open-Meteo 외부 API 호출 클라이언트 */
@Component
@Slf4j
public class OpenMeteoClient {

    /** Open-Meteo 날씨 API 기본 URL */
    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1";
    /** Open-Meteo 대기질 API 기본 URL */
    private static final String FINE_DUST_BASE_URL = "https://air-quality-api.open-meteo.com/v1";
    /** 연결 타임아웃(밀리초) */
    private static final int CONNECT_TIMEOUT_MS = 3000;
    /** 응답 타임아웃(밀리초) */
    private static final int READ_TIMEOUT_MS = 5000;
    /** 타임아웃이 적용된 RestTemplate */
    private final RestTemplate restTemplate = createRestTemplate();

    /** 날씨 API 호출 */
    public Optional<Map<String, Object>> fetchWeather(Double latitude, Double longitude, String queryParam) {
        return callApi(WEATHER_BASE_URL, "/forecast", latitude, longitude, queryParam);
    }

    /** 대기질 API 호출 */
    public Optional<Map<String, Object>> fetchFineDust(Double latitude, Double longitude, String queryParam) {
        return callApi(FINE_DUST_BASE_URL, "/air-quality", latitude, longitude, queryParam);
    }

    /** 공통 GET 호출 후 응답 바디 반환 */
    private Optional<Map<String, Object>> callApi(
            String baseUrl,
            String path,
            Double latitude,
            Double longitude,
            String queryParam
    ) {
        try {
            String apiUri = buildUrl(baseUrl, path, latitude, longitude, queryParam);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    new URI(apiUri),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(responseBody);
        } catch (HttpStatusCodeException e) {
            log.error("Open-Meteo 상태코드 오류: {}", e.getStatusCode(), e);
            throw ExternalApiException.badGateway("외부 API 응답 오류가 발생했습니다.");
        } catch (ResourceAccessException e) {
            if (isTimeoutException(e)) {
                log.error("Open-Meteo 타임아웃 발생", e);
                throw ExternalApiException.gatewayTimeout("외부 API 호출 시간이 초과되었습니다.");
            }
            log.error("Open-Meteo 연결 오류 발생", e);
            throw ExternalApiException.badGateway("외부 API 연결 오류가 발생했습니다.");
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Open-Meteo 호출 중 예기치 못한 오류", e);
            throw ExternalApiException.badGateway("외부 API 호출 중 오류가 발생했습니다.");
        }
    }

    /** 호출 URL 생성 */
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

    /** 연결/응답 타임아웃 설정이 반영된 RestTemplate 생성 */
    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        requestFactory.setReadTimeout(READ_TIMEOUT_MS);
        return new RestTemplate(requestFactory);
    }

    /** 예외 체인에 SocketTimeoutException이 있으면 true 반환 */
    private boolean isTimeoutException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof SocketTimeoutException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
