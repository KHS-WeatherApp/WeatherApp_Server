package com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.client;

import com.server.KH_StudyProjects_WeatherServer.global.exception.ExternalApiException;
import com.server.KH_StudyProjects_WeatherServer.global.logging.LogCode;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.dto.OpenMeteoApiResponseDto;
import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.mapper.OpenMeteoResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class OpenMeteoClient {

    /** Open-Meteo 날씨 API 기본 URL */
    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1";
    /** Open-Meteo 대기질 API 기본 URL */
    private static final String FINE_DUST_BASE_URL = "https://air-quality-api.open-meteo.com/v1";
    /** Open-Meteo 전용 RestTemplate */
    @Qualifier("openMeteoRestTemplate")
    private final RestTemplate restTemplate;

    /** 날씨 API 호출 */
    public Optional<OpenMeteoApiResponseDto> fetchWeather(Double latitude, Double longitude, String queryParam) {
        return callApi(WEATHER_BASE_URL, "/forecast", latitude, longitude, queryParam);
    }

    /** 대기질 API 호출 */
    public Optional<OpenMeteoApiResponseDto> fetchFineDust(Double latitude, Double longitude, String queryParam) {
        return callApi(FINE_DUST_BASE_URL, "/air-quality", latitude, longitude, queryParam);
    }

    /** 공통 GET 호출 후 응답 바디 반환 */
    private Optional<OpenMeteoApiResponseDto> callApi(
            String baseUrl,
            String path,
            Double latitude,
            Double longitude,
            String queryParam
    ) {
        try {
            log.info("[{}] openmeteo.request path={} latitude={} longitude={} queryLength={}",
                    LogCode.EXT_REQ_001,
                    path,
                    latitude,
                    longitude,
                    queryParam != null ? queryParam.length() : 0);

            String apiUri = buildUrl(baseUrl, path, latitude, longitude, queryParam);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    new URI(apiUri),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                log.warn("[{}] openmeteo.empty-response path={} latitude={} longitude={}",
                        LogCode.EXT_204_001, path, latitude, longitude);
                return Optional.empty();
            }
            OpenMeteoApiResponseDto mappedResponse = OpenMeteoResponseMapper.fromRawMap(responseBody, path);
            log.info("[{}] openmeteo.success path={} status={} keys={}",
                    LogCode.EXT_200_001,
                    path,
                    response.getStatusCode().value(),
                    mappedResponse.toApiPayload().keySet());
            return Optional.of(mappedResponse);
        } catch (HttpStatusCodeException e) {
            log.error("[{}] openmeteo.http-status-fail path={} status={} message={}",
                    LogCode.EXT_502_001, path, e.getStatusCode().value(), e.getMessage(), e);
            throw ExternalApiException.badGateway("외부 API 응답 오류가 발생했습니다.");
        } catch (ResourceAccessException e) {
            if (isTimeoutException(e)) {
                log.error("[{}] openmeteo.timeout path={} message={}",
                        LogCode.EXT_504_001, path, e.getMessage(), e);
                throw ExternalApiException.gatewayTimeout("외부 API 호출 시간이 초과되었습니다.");
            }
            log.error("[{}] openmeteo.connection-fail path={} message={}",
                    LogCode.EXT_502_002, path, e.getMessage(), e);
            throw ExternalApiException.badGateway("외부 API 연결 오류가 발생했습니다.");
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("[{}] openmeteo.unexpected-fail path={} message={}",
                    LogCode.EXT_502_003, path, e.getMessage(), e);
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
