package com.server.KH_StudyProjects_WeatherServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 서버 애플리케이션 시작점 */
@SpringBootApplication(scanBasePackages = {
        "com.server.KH_StudyProjects_WeatherServer.domain",
        "com.server.KH_StudyProjects_WeatherServer.global",
        "com.server.KH_StudyProjects_WeatherServer.infrastructure"
})
public class KhStudyProjectsWeatherServerApplication {

    /** Spring Boot 애플리케이션 실행 */
    public static void main(String[] args) {
        SpringApplication.run(KhStudyProjectsWeatherServerApplication.class, args);
    }
}
