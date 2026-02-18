# KHS WeatherApp Server

Spring Boot 기반 팀 프로젝트 백엔드 서버입니다.  
Android 앱과 연동해 Open-Meteo 날씨/대기질 데이터를 조회하고, 사이드메뉴 즐겨찾기를 관리합니다.

## 주요 기능
- 날씨 조회 프록시: `POST /api/weather`
- 대기질 조회 프록시: `POST /api/airPollution`
- 즐겨찾기 관리: 추가/조회/삭제/정렬/중복 확인 (`/api/sidemenu/**`)
- 전역 예외 처리 및 로그 코드 표준화

## 기술 스택
- Java 17
- Spring Boot 3.3.4
- Gradle
- MyBatis
- Oracle JDBC (ojdbc11), HikariCP
- Lombok, SLF4J

## 패키지 구조
```text
src/main/java/com/server/KH_StudyProjects_WeatherServer
├── domain
│   ├── weather      # 날씨 도메인
│   ├── finedust     # 대기질 도메인
│   ├── favorite     # 사이드메뉴 즐겨찾기 도메인
│   └── setting      # 설정 도메인(스캐폴드)
├── global
│   ├── config       # 공통 설정 (OpenMeteo RestTemplate Bean)
│   ├── exception    # 전역 예외 처리
│   ├── logging      # 로그 코드 상수
│   └── util         # 공통 응답 래퍼(ApiResponse)
├── infrastructure
│   └── openmeteo
│       ├── client   # Open-Meteo 호출 클라이언트
│       ├── dto      # Open-Meteo 내부 응답 DTO
│       └── mapper   # raw Map -> DTO 매핑
└── KhStudyProjectsWeatherServerApplication.java
```

## API 응답 규약 (현재)
| API 그룹 | 응답 형식 | 비고 |
|---|---|---|
| `/api/weather` | raw JSON(Map) | Android 로딩 호환 모드 |
| `/api/airPollution` | raw JSON(Map) | Android 로딩 호환 모드 |
| `/api/sidemenu/**` | `ApiResponse<T>` | 표준 규약 적용 |
| 전역 예외 응답 | `ApiResponse<Void>` | `GlobalExceptionHandler` 기준 |

참고:
- `/api/airPollution`은 외부 API URL이 아니라 서버 내부 엔드포인트입니다.
- 구현 패키지는 `domain.finedust`이며 앱의 날씨 화면에서도 사용됩니다.

## API 요약

### Open-Meteo 프록시
| Method | Path | 설명 | 요청 Body |
|---|---|---|---|
| `POST` | `/api/weather` | 날씨 조회 | JSON |
| `POST` | `/api/airPollution` | 대기질 조회 | JSON |

요청 예시:
```json
{
  "latitude": 37.5665,
  "longitude": 126.9780,
  "queryParam": "hourly=pm10,pm2_5&timezone=auto"
}
```

외부 연동:
- Weather: `https://api.open-meteo.com/v1/forecast`
- Air Quality: `https://air-quality-api.open-meteo.com/v1/air-quality`

### 즐겨찾기 (Side Menu)
기본 경로: `/api/sidemenu`

| Method | Path | 설명 | 응답 |
|---|---|---|---|
| `POST` | `/locations` | 즐겨찾기 추가 | `ApiResponse<SmFavoriteLocationResponseDto>` |
| `GET` | `/locations?deviceId={deviceId}` | 즐겨찾기 목록 조회 | `ApiResponse<List<SmFavoriteLocationResponseDto>>` |
| `DELETE` | `/locations?latitude={lat}&longitude={lng}&deviceId={deviceId}` | 즐겨찾기 삭제 | `ApiResponse<Void>` |
| `PATCH` | `/locations/sort-order?latitude={lat}&longitude={lng}&deviceId={deviceId}&sortOrder={n}` | 정렬 변경 | `ApiResponse<Void>` |
| `GET` | `/locations/check-duplicate?latitude={lat}&longitude={lng}&deviceId={deviceId}` | 중복 확인 | `ApiResponse<Boolean>` |

## 실행 방법
사전 조건:
- JDK 17+
- Oracle DB 접근 가능 환경

실행:
```bash
# Windows
gradlew.bat bootRun

# Mac/Linux
./gradlew bootRun
```

테스트:
```bash
# Windows
gradlew.bat test

# Mac/Linux
./gradlew test
```

기본 주소:
- `http://localhost:8080`

## 설정
주요 파일:
- `src/main/resources/application.yml`
- `src/main/resources/mybatis-config.xml`

주요 프로퍼티:
- `server.port`
- `spring.datasource.*`
- `mybatis.config-location`
- `mybatis.mapper-locations`
- `openmeteo.client.connect-timeout-ms`
- `openmeteo.client.read-timeout-ms`

보안 주의:
- 현재 `application.yml`에 DB 계정 정보가 포함되어 있습니다.
- 운영 전 반드시 환경변수/프로파일 분리로 민감정보를 외부화해야 합니다.

## 트러블슈팅
- `No static resource api/airPollution` 발생 시:
  - `POST /api/airPollution`로 요청했는지 확인
  - IDE 산출물(`out/production/classes`) 캐시 정리 후 재빌드
  - `KhStudyProjectsWeatherServerApplication`의 `scanBasePackages` 확인
- 앱 로딩 화면에서 멈출 때:
  - weather/finedust 응답 형식이 앱 파서와 호환되는지 확인
  - 현재 서버는 호환을 위해 두 API를 raw JSON으로 반환
