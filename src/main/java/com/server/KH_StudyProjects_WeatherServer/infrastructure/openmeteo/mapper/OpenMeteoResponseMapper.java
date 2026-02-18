package com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.mapper;

import com.server.KH_StudyProjects_WeatherServer.infrastructure.openmeteo.dto.OpenMeteoApiResponseDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Open-Meteo raw 응답(Map)을 내부 DTO로 매핑한다. */
public final class OpenMeteoResponseMapper {

    private static final Set<String> KNOWN_KEYS = Set.of(
            "latitude",
            "longitude",
            "generationtime_ms",
            "utc_offset_seconds",
            "timezone",
            "timezone_abbreviation",
            "elevation",
            "current_units",
            "current",
            "hourly_units",
            "hourly",
            "daily_units",
            "daily"
    );

    private OpenMeteoResponseMapper() {
    }

    /** Open-Meteo raw 응답을 표준 DTO로 변환한다. */
    public static OpenMeteoApiResponseDto fromRawMap(Map<String, Object> raw, String endpointPath) {
        Map<String, Object> source = raw != null ? raw : Map.of();

        return OpenMeteoApiResponseDto.builder()
                .endpointPath(endpointPath)
                .latitude(asDouble(source.get("latitude")))
                .longitude(asDouble(source.get("longitude")))
                .generationtimeMs(asDouble(source.get("generationtime_ms")))
                .utcOffsetSeconds(asInteger(source.get("utc_offset_seconds")))
                .timezone(asString(source.get("timezone")))
                .timezoneAbbreviation(asString(source.get("timezone_abbreviation")))
                .elevation(asDouble(source.get("elevation")))
                .currentUnits(asMap(source.get("current_units")))
                .current(asMap(source.get("current")))
                .hourlyUnits(asMap(source.get("hourly_units")))
                .hourly(asMap(source.get("hourly")))
                .dailyUnits(asMap(source.get("daily_units")))
                .daily(asMap(source.get("daily")))
                .extraFields(extractExtraFields(source))
                .build();
    }

    private static Map<String, Object> extractExtraFields(Map<String, Object> source) {
        Map<String, Object> extra = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            if (!KNOWN_KEYS.contains(entry.getKey())) {
                extra.put(entry.getKey(), entry.getValue());
            }
        }
        return extra;
    }

    private static String asString(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    private static Double asDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer asInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return new LinkedHashMap<>((Map<String, Object>) map);
        }
        return null;
    }
}
