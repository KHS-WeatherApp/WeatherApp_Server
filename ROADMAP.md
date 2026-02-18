# KHS-WeatherApp Server ROADMAP v2.4

작성일: 2026-02-18  
최종 수정: 2026-02-19 (KST)

## 0. 문서 목적
- 팀 프로젝트 기준으로 서버 구조/안정성/운영 준비 항목을 한 문서에서 관리한다.
- 현재 상태, 남은 작업, 우선순위를 빠르게 파악할 수 있게 유지한다.

## 1. 진행 현황 요약

### Phase 상태
- [ ] Phase 0: 보안/설정 핫픽스
- [ ] Phase 1: 안정성/API 계약 정리
- [ ] Phase 2: Open-Meteo 모듈 고도화
- [x] Phase 3: 패키지/네이밍 리팩터링
- [ ] Phase 4: 품질/운영 체계 강화

### 현재 완료된 핵심 항목
- [x] 패키지 구조 재배치 (`domain / global / infrastructure`)
- [x] 레거시 `flag` 제거 + `OpenMeteoClient` 분리
- [x] 외부 API 실패 응답 정책 분리 (`404`, `502`, `504`)
- [x] 커스텀 예외 도입 (`DuplicateLocationException`, `LocationNotFoundException`)
- [x] `MethodArgumentNotValidException` 400 표준 처리
- [x] 로그 문구/코드 표준화 (`[코드] 이벤트 key=value`)
- [x] Open-Meteo 응답 DTO 매핑 계층 도입
- [x] Open-Meteo HTTP Client 설정 중앙화(Bean)
- [x] 최소 테스트 세트 구축
  - `GlobalExceptionHandlerTest`
  - `WeatherControllerTest`
  - `FineDustControllerTest`
  - `SmFavoriteLocationServiceTest`
  - `OpenMeteoResponseMapperTest`

### 아직 남은 핵심 항목
- [ ] DB 민감정보 외부화 + 프로파일 분리
- [ ] `queryParam` 가드(허용 필드/형식/길이 제한)
- [ ] 통합/회귀 테스트 범위 확대
- [ ] Android 호환 모드 종료 후 weather/finedust 응답 `ApiResponse` 재전환

## 2. 현재 API 계약 (중요)

| API 그룹 | 현재 응답 형식 | 비고 |
|---|---|---|
| `/api/weather` | raw JSON(Map) | Android 로딩 호환을 위해 유지 |
| `/api/airPollution` | raw JSON(Map) | Android 로딩 호환을 위해 유지 |
| `/api/sidemenu/**` | `ApiResponse` | 신규 규약 적용 |
| 전역 예외 응답 | `ApiResponse` | `GlobalExceptionHandler` 기준 |

## 3. 현재 아키텍처

```text
src/main/java/com/server/KH_StudyProjects_WeatherServer/
├── domain/
│   ├── weather/
│   ├── finedust/
│   ├── favorite/
│   └── setting/ (placeholder)
├── global/
│   ├── config/
│   ├── exception/
│   ├── logging/
│   └── util/
└── infrastructure/
    └── openmeteo/
        ├── client/
        ├── dto/
        └── mapper/
```

## 4. Phase별 상세 계획

## Phase 0. 보안/설정 핫픽스
목표: 운영 전 필수 리스크 제거

작업:
- `application.yml` 민감정보 제거
- `application-local.yml`, `application-prod.yml` 분리
- 환경변수/시크릿 기반 설정 주입 전환

완료 기준:
- 저장소에 비밀값 없음
- 로컬/운영 프로파일 모두 부팅 확인

## Phase 1. 안정성/API 계약 정리
목표: 예측 가능한 오류 응답과 입력 방어 강화

완료:
- [x] 외부 API 오류 상태코드 정책 정리
- [x] 커스텀 예외 + 전역 핸들러 정리
- [x] DTO + `@Valid` 기반 요청 검증
- [x] 검증 실패 400 표준화

진행중/남은 작업:
- [ ] `queryParam` 가드 로직 추가
- [ ] weather/finedust 응답 `ApiResponse` 재전환 시점 확정(Android 팀 합의 필요)

완료 기준:
- 비정상 파라미터가 서버 단에서 차단됨
- 앱/서버 간 응답 계약이 문서와 일치

## Phase 2. Open-Meteo 모듈 고도화
목표: 외부 연동 계층 책임 분리 및 안정화

완료:
- [x] `OpenMeteoClient` 분리
- [x] weather/finedust 서비스 분리
- [x] 외부 응답 -> 내부 DTO 매핑 계층
- [x] HTTP Client Bean 설정 중앙화

남은 작업:
- [ ] 필요 시 `RestTemplate` -> `WebClient` 전환 검토

완료 기준:
- 외부 API 변경 영향이 도메인에 직접 전파되지 않음
- 클라이언트/서비스 경계가 명확함

## Phase 3. 패키지/네이밍 리팩터링
목표: 팀 온보딩/유지보수성 개선

완료:
- [x] `common.sidemenu` -> `domain.favorite`
- [x] `common.exception|util` -> `global.exception|util`
- [x] `Sm = SideMenu` 네이밍 정책 유지
- [x] 라우팅 컨벤션 문서 반영

## Phase 4. 품질/운영 체계 강화
목표: 릴리즈 안정성 및 운영 품질 확보

작업:
- 테스트 확대(Controller/Service/Mapper/Integration)
- CI 품질 게이트(test, 정적검사, 문서 동기화)
- 장애 대응 문서화(로그 코드/에러 시나리오)

완료 기준:
- 핵심 시나리오 회귀 테스트 자동화
- 배포 전 점검 체크리스트 정착

## 5. 이번 스프린트 TODO (우선순위)
1. DB 민감정보 외부화 + 프로파일 분리
2. `queryParam` 가드 추가
3. Android 팀과 응답 재전환 일정/조건 확정

## 6. 백업 및 추적 정보
- 레거시 백업: `backup/legacy-package-snapshot_2026-02-19`
- 구조 스냅샷: `backup/legacy-package-snapshot_2026-02-19/PACKAGE_STRUCTURE_TREE.txt`

## 7. 팀 운영 규칙
- 구조 리팩터링 PR과 기능 변경 PR은 분리한다.
- API 계약 변경은 Android 팀과 동시 공유/적용한다.
- Phase 종료 시 README/ROADMAP/테스트를 함께 업데이트한다.
