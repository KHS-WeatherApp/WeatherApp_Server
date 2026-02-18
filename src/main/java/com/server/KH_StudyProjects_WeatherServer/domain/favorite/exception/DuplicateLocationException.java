package com.server.KH_StudyProjects_WeatherServer.domain.favorite.exception;

/** 즐겨찾기 위치 중복 예외 */
public class DuplicateLocationException extends RuntimeException {

    /** 중복 예외 생성 */
    public DuplicateLocationException(String message) {
        super(message);
    }
}
