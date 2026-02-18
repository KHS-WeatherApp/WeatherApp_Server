package com.server.KH_StudyProjects_WeatherServer.global.exception;

import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.DuplicateLocationException;
import com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception.LocationNotFoundException;
import com.server.KH_StudyProjects_WeatherServer.global.util.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("중복 위치 예외는 409를 반환한다")
    void duplicateLocationExceptionReturns409() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleDuplicateLocationException(new DuplicateLocationException("중복"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("중복");
    }

    @Test
    @DisplayName("위치 없음 예외는 404를 반환한다")
    void locationNotFoundExceptionReturns404() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleLocationNotFoundException(new LocationNotFoundException("없음"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("없음");
    }

    @Test
    @DisplayName("외부 API 502 예외는 502를 반환한다")
    void externalApiBadGatewayReturns502() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleExternalApiException(ExternalApiException.badGateway("연결 오류"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("연결 오류");
    }

    @Test
    @DisplayName("외부 API 504 예외는 504를 반환한다")
    void externalApiGatewayTimeoutReturns504() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleExternalApiException(ExternalApiException.gatewayTimeout("타임아웃"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GATEWAY_TIMEOUT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("타임아웃");
    }
}
