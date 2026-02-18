package com.server.KH_StudyProjects_WeatherServer.domain.favorite.service;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.dto.SmFavoriteLocationRequestDto;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.DuplicateLocationException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.LocationNotFoundException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.mapper.SmFavoriteLocationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmFavoriteLocationServiceTest {

    private static final String DEVICE_ID = "device-1";
    private static final Double LAT = 37.5665;
    private static final Double LNG = 126.9780;

    @Mock
    private SmFavoriteLocationMapper smFavoriteLocationMapper;

    @InjectMocks
    private SmFavoriteLocationService smFavoriteLocationService;

    @Test
    @DisplayName("중복 위치면 DuplicateLocationException을 던진다")
    void addSmFavoriteLocationDuplicateThrowsException() {
        SmFavoriteLocationRequestDto requestDto = SmFavoriteLocationRequestDto.builder()
                .addressName("서울")
                .latitude(LAT)
                .longitude(LNG)
                .deviceId(DEVICE_ID)
                .build();

        when(smFavoriteLocationMapper.existsByLatitudeAndLongitude(LAT, LNG, DEVICE_ID)).thenReturn(true);

        assertThatThrownBy(() -> smFavoriteLocationService.addSmFavoriteLocation(requestDto))
                .isInstanceOf(DuplicateLocationException.class);

        verify(smFavoriteLocationMapper, never()).insertFavoriteLocation(any());
    }

    @Test
    @DisplayName("삭제 대상이 없으면 LocationNotFoundException을 던진다")
    void deleteSmFavoriteLocationNotFoundThrowsException() {
        when(smFavoriteLocationMapper.selectFavoriteLocationsByUserId(DEVICE_ID)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> smFavoriteLocationService.deleteSmFavoriteLocation(LAT, LNG, DEVICE_ID))
                .isInstanceOf(LocationNotFoundException.class);

        verify(smFavoriteLocationMapper, never()).deleteFavoriteLocation(anyString(), anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("정렬 변경 대상이 없으면 LocationNotFoundException을 던진다")
    void updateSortOrderNotFoundThrowsException() {
        when(smFavoriteLocationMapper.updateFavoriteLocationSortOrder(DEVICE_ID, LAT, LNG, 1)).thenReturn(0);

        assertThatThrownBy(() -> smFavoriteLocationService.updateSmFavoriteLocationSortOrder(LAT, LNG, DEVICE_ID, 1))
                .isInstanceOf(LocationNotFoundException.class);
    }
}
