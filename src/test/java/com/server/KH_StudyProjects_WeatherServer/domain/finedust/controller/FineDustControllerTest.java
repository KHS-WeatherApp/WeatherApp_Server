package com.server.KH_StudyProjects_WeatherServer.domain.finedust.controller;

import com.server.KH_StudyProjects_WeatherServer.domain.finedust.dto.FineDustRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.finedust.service.FineDustService;
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

@WebMvcTest(controllers = FineDustController.class)
class FineDustControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FineDustService fineDustService;

    @Test
    @DisplayName("정상 요청이면 대기질 조회가 성공한다")
    void getFineDustSuccess() throws Exception {
        doReturn(ResponseEntity.ok(Map.of("source", "openmeteo-air")))
                .when(fineDustService).getFineDust(any(FineDustRequestDto.class));

        mockMvc.perform(post("/api/airPollution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "latitude": 37.5665,
                                  "longitude": 126.9780,
                                  "queryParam": "hourly=pm10,pm2_5&timezone=auto"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("openmeteo-air"));

        verify(fineDustService).getFineDust(any(FineDustRequestDto.class));
    }

    @Test
    @DisplayName("필수값 누락 요청이면 400을 반환한다")
    void getFineDustValidationFail() throws Exception {
        mockMvc.perform(post("/api/airPollution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "latitude": 37.5665,
                                  "longitude": 126.9780
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(fineDustService, never()).getFineDust(any(FineDustRequestDto.class));
    }
}
