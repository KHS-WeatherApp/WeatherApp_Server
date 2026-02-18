package com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/** Open-Meteo 외부 응답을 내부에서 다루기 위한 DTO */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenMeteoApiResponseDto {

    /** 호출된 Open-Meteo 경로 */
    private String endpointPath;
    /** 위도 */
    private Double latitude;
    /** 경도 */
    private Double longitude;
    /** 응답 생성 시간(ms) */
    private Double generationtimeMs;
    /** UTC 오프셋(초) */
    private Integer utcOffsetSeconds;
    /** 타임존 */
    private String timezone;
    /** 타임존 약어 */
    private String timezoneAbbreviation;
    /** 고도 */
    private Double elevation;
    /** current 단위 */
    private Map<String, Object> currentUnits;
    /** current 데이터 */
    private Map<String, Object> current;
    /** hourly 단위 */
    private Map<String, Object> hourlyUnits;
    /** hourly 데이터 */
    private Map<String, Object> hourly;
    /** daily 단위 */
    private Map<String, Object> dailyUnits;
    /** daily 데이터 */
    private Map<String, Object> daily;
    /** 표준 필드 외의 추가 응답 데이터 */
    private Map<String, Object> extraFields;

    /** API 응답 규약에 실어 보낼 payload 맵 생성 */
    public Map<String, Object> toApiPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();

        putIfNotNull(payload, "latitude", latitude);
        putIfNotNull(payload, "longitude", longitude);
        putIfNotNull(payload, "generationtime_ms", generationtimeMs);
        putIfNotNull(payload, "utc_offset_seconds", utcOffsetSeconds);
        putIfNotNull(payload, "timezone", timezone);
        putIfNotNull(payload, "timezone_abbreviation", timezoneAbbreviation);
        putIfNotNull(payload, "elevation", elevation);
        putIfNotNull(payload, "current_units", currentUnits);
        putIfNotNull(payload, "current", current);
        putIfNotNull(payload, "hourly_units", hourlyUnits);
        putIfNotNull(payload, "hourly", hourly);
        putIfNotNull(payload, "daily_units", dailyUnits);
        putIfNotNull(payload, "daily", daily);

        if (extraFields != null && !extraFields.isEmpty()) {
            payload.putAll(extraFields);
        }
        return payload;
    }

    private void putIfNotNull(Map<String, Object> payload, String key, Object value) {
        if (value != null) {
            payload.put(key, value);
        }
    }
}
