package com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception;

/** 즐겨찾기 위치 미존재 예외 */
public class LocationNotFoundException extends RuntimeException {

    /** 미존재 예외 생성 */
    public LocationNotFoundException(String message) {
        super(message);
    }
}
