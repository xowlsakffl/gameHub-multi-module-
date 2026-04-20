# GameHub 멀티 모듈 웹 솔루션 API

GameHub는 디스코드형 게임 파티 운영을 위한 멀티 모듈 기반 웹 서비스입니다. 이 저장소는 사용자 인증, 파티 생성/참가, 친구 기능, 실시간 채팅, 음성채널 상태, 권한/초대/뮤트 관리까지 포함한 Spring Boot + React 구조를 제공합니다.

포트폴리오 관점에서 핵심은 단순 CRUD를 넘어, 모듈 분리(`domain/core/user-api/admin-api`)를 유지하면서 JWT 인증, WebSocket(STOMP) 실시간 이벤트, 파티 멤버 권한 정책(방장/운영진/일반), 초대코드 기반 진입 흐름을 실제 서비스 형태로 통합한 점입니다.

GameHub는 게임별 파티 모집과 운영을 빠르게 처리하도록 설계되었습니다. 파티 타입(자동참가/승인참가), 친구 요청/수락, 파티 멤버 관리, 실시간 채팅/읽음 상태, 음성채널 접속 상태 브로드캐스트, 사용자별 푸시 알림까지 하나의 허브 경험으로 구성했습니다.

이 저장소는 그 서비스 중 API/도메인/프론트엔드 빌드 통합 계층을 담당합니다. 사용자 앱은 REST + WebSocket endpoint를 사용해 로그인 상태, 파티 상태, 실시간 메시지, 음성채널 변화를 동기화합니다.

## 주요 기능

- 회원가입 및 로그인 API (JWT)
- 친구 요청/수락/거절/목록 조회
- 파티 생성/조회/수정/삭제/상태 변경
- 자동참가/승인참가 요청 및 승인/거절
- 파티 멤버 관리 (나가기, 강퇴, 방장 위임)
- 실시간 파티 채팅(WebSocket) + 읽음 상태
- 음성채널 입장/퇴장/채널 변경 상태 브로드캐스트
- 사용자별 WebSocket 푸시 알림(`/user/queue/notifications`)
- 파티 권한 체계 (`LEADER` / `MANAGER` / `MEMBER`)
- 파티 멤버 뮤트/해제 및 뮤트 상태 차단(채팅/음성)
- 파티 초대코드 조회/재발급/초대코드 입장

## 기술 스택

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-010101?style=for-the-badge&logo=socketdotio&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-7-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

- Java 21
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- Spring WebSocket (STOMP, SockJS)
- React + Vite
- Gradle Multi Module

## 프로젝트 구조

```text
onion/
├── onion-domain/                  # Entity, Enum, Repository
├── onion-core/                    # DTO, Service, Security, Exception
├── onion-user-api/                # 사용자 API, WebSocket config/controller
├── onion-admin-api/               # 관리자 API 모듈
├── front-end/                     # React(Vite) 프론트엔드
├── postman/                       # Postman 컬렉션
├── build.gradle                   # 루트 Gradle 설정
└── settings.gradle                # 멀티 모듈 구성
```

## 실행 준비

```bash
./gradlew build
./gradlew :onion-user-api:bootRun
```

PowerShell:

```powershell
.\gradlew.bat build
.\gradlew.bat :onion-user-api:bootRun
```

프론트 단독 실행:

```bash
cd front-end
npm install
npm run dev
```

## 주요 환경변수

| 변수 | 설명 |
| --- | --- |
| `SERVER_PORT` | `onion-user-api` 서버 포트 |
| `DB_URL` | DB JDBC URL |
| `DB_DRIVER` | DB 드라이버 클래스명 |
| `DB_USERNAME` | DB 계정 |
| `DB_PASSWORD` | DB 비밀번호 |
| `JWT_SECRET` | JWT 서명 키 |
| `JWT_EXPIRATION` | JWT 만료 시간(ms) |

## 보완한 부분

- 프론트 산출물 복사 경로를 `build`에서 `dist`로 정정
- 사용자 API 정적 리소스 산출물 추적 정책 정리
- 파티 상태 변경 시 잘못된 status 입력 500 오류를 400 처리로 보완
- 파티/친구/파티 쓰기 API 인증 정책 강화
- 파티 생성/참가 요청 입력값 검증 추가
- 참가 요청 중복 검사 로직을 존재 쿼리 기반으로 최적화
- WebSocket JWT 인증 인터셉터 및 인바운드 채널 보안 추가
- 실시간 채팅 + 읽음 상태 저장/조회 API 추가
- 음성채널 접속 상태 + 파티 브로드캐스트 + 사용자 푸시 알림 추가
- 권한 체계 확장(`LEADER`/`MANAGER`/`MEMBER`) 및 강퇴 정책 세분화
- 파티 뮤트/해제 기능 및 뮤트 시 채팅/음성 차단 적용
- 파티 초대코드 조회/재발급/입장 흐름 추가

## 관련 저장소

- 메인 원격: `https://github.com/xowlsakffl/gameHub-multi-module-.git`
- 본 저장소는 `domain/core/user-api/admin-api/front-end`를 포함한 통합 모노레포입니다.
