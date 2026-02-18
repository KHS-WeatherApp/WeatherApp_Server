package com.server.KH_StudyProjects_WeatherServer.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/** Open-Meteo 외부 호출용 HTTP 클라이언트 설정 */
@Configuration
public class OpenMeteoHttpClientConfig {

    /** 연결 타임아웃(밀리초) */
    @Value("${openmeteo.client.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    /** 응답 타임아웃(밀리초) */
    @Value("${openmeteo.client.read-timeout-ms:5000}")
    private int readTimeoutMs;

    /** Open-Meteo 전용 RestTemplate Bean */
    @Bean("openMeteoRestTemplate")
    public RestTemplate openMeteoRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutMs);
        requestFactory.setReadTimeout(readTimeoutMs);
        return new RestTemplate(requestFactory);
    }
}
