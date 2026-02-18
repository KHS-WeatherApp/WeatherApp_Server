package com.server.KH_StudyProjects_WeatherServer.domain.weather.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.weather.dto.WeatherRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.weather.service.WeatherService;
import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    @DisplayName("정상 요청이면 날씨 조회가 성공한다")
    void getWeatherSuccess() throws Exception {
        doReturn(ResponseEntity.ok(ApiResponse.success("날씨 정보를 성공적으로 조회했습니다.", Map.of("source", "openmeteo"))))
                .when(weatherService).getWeather(any(WeatherRequestDto.class));

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "latitude": 37.5665,
                                  "longitude": 126.9780,
                                  "queryParam": "hourly=temperature_2m&timezone=auto"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.source").value("openmeteo"));

        verify(weatherService).getWeather(any(WeatherRequestDto.class));
    }

    @Test
    @DisplayName("필수값 누락 요청이면 400을 반환한다")
    void getWeatherValidationFail() throws Exception {
        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "latitude": 37.5665,
                                  "longitude": 126.9780
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(weatherService, never()).getWeather(any(WeatherRequestDto.class));
    }
}
