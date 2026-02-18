package com.server.KH_StudyProjects_WeatherServer.global.exception;

import org.springframework.http.HttpStatus;

/** 외부 API 호출 실패를 표현하는 예외 */
public class ExternalApiException extends RuntimeException {

    /** 클라이언트에 반환할 HTTP 상태코드 */
    private final HttpStatus status;

    /**
     * 외부 API 예외 생성자
     *
     * @param status 반환할 HTTP 상태코드
     * @param message 예외 메시지
     */
    public ExternalApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /** 예외에 매핑된 HTTP 상태코드 반환 */
    public HttpStatus getStatus() {
        return status;
    }

    /** 502 Bad Gateway 예외 생성 */
    public static ExternalApiException badGateway(String message) {
        return new ExternalApiException(HttpStatus.BAD_GATEWAY, message);
    }

    /** 504 Gateway Timeout 예외 생성 */
    public static ExternalApiException gatewayTimeout(String message) {
        return new ExternalApiException(HttpStatus.GATEWAY_TIMEOUT, message);
    }
}
