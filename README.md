# GameHub 멀티 모듈 웹 솔루션

![GameHub 사용자 화면](https://dummyimage.com/1200x630/101826/ffffff&text=GameHub+Party+Hub)

GameHub는 디스코드형 게임 파티 운영을 위한 멀티 모듈 기반 웹 서비스입니다. 이 저장소는 사용자 인증, 친구 기능, 파티 모집/참가, 실시간 채팅, 음성채널 상태, 권한/초대/뮤트 관리, 프론트엔드 번들 통합까지 포함한 Spring Boot + React 모노레포입니다.

핵심은 `domain / core / user-api / admin-api / front-end`로 역할을 분리한 상태에서 JWT 인증, WebSocket(STOMP) 실시간 이벤트, 파티 멤버 권한 정책, 초대코드 기반 진입 흐름, 프론트 빌드 산출물의 백엔드 통합까지 실제 서비스 구조로 연결했습니다.

## 프로젝트 개요

- 회원가입 및 로그인 기반 사용자 인증
- 친구 요청/수락/거절/목록 관리
- 게임 파티 생성/조회/수정/삭제/상태 변경
- 자동참가/승인참가 방식의 파티 참여 흐름
- 파티 채팅 및 읽음 상태 관리
- 음성채널 입장/퇴장/이동 상태 브로드캐스트
- 사용자별 실시간 푸시 알림
- 초대코드 조회/재발급/코드 입장
- React 프론트엔드와 Spring Boot API의 통합 배포 구조

## 이 저장소가 맡는 역할

GameHub 서비스는 크게 다음 계층으로 나뉩니다.

- 도메인 계층: 엔티티, 리포지토리, 데이터 모델
- 코어 계층: 서비스, DTO, 예외, 보안 정책
- 사용자 API 계층: 인증, 파티, 친구, WebSocket 실시간 기능
- 관리자 API 계층: 운영 확장용 백엔드 모듈
- 프론트엔드 계층: React 기반 사용자 UI

이 저장소는 위 계층을 하나의 모노레포에서 함께 관리합니다. 특히 `onion-user-api`가 프론트엔드 빌드를 받아 정적 리소스로 포함하는 구조라서, 개발과 배포를 한 프로젝트 흐름으로 묶어두었다는 점이 특징입니다.

## 핵심 서비스 흐름

1. 사용자가 `/api/auth/signup`, `/api/auth/login`으로 가입하거나 로그인합니다.
2. JWT 기반 인증 상태로 친구, 파티, 채팅, 음성채널 API를 호출합니다.
3. 사용자는 파티를 생성하거나 초대코드/참가 요청을 통해 파티에 들어갑니다.
4. 파티 멤버 권한(`LEADER`, `MANAGER`, `MEMBER`)에 따라 수정, 승인, 강퇴, 위임, 뮤트가 적용됩니다.
5. 채팅은 REST 조회와 STOMP 실시간 송수신을 함께 사용하고, 읽음 상태도 별도로 관리됩니다.
6. 음성채널 입장/퇴장/이동 이벤트는 파티 토픽과 사용자 전용 큐로 브로드캐스트됩니다.
7. 프론트엔드는 Vite로 빌드되고, 빌드 결과는 `onion-user-api`의 정적 리소스로 복사되어 함께 서빙됩니다.

## 주요 기능

### 1. 인증 및 사용자 계정

- 회원가입
- 로그인 및 JWT 발급
- 이메일/닉네임 중복 확인

관련 컨트롤러:

- `AuthController`

주요 경로:

- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/auth/check-email`
- `GET /api/auth/check-nickname`

### 2. 친구 기능

- 친구 요청 전송
- 친구 수락/거절
- 친구 목록 조회

관련 컨트롤러:

- `FriendController`

### 3. 파티 모집 및 참여

- 파티 생성/수정/삭제
- 파티 목록 페이지 조회
- 파티 상세 조회
- 파티 상태 변경
- 자동참가/승인참가 요청
- 참가 요청 승인/거절
- 초대코드 조회/재발급/입장

관련 컨트롤러:

- `GamePartyController`
- `PartyJoinRequestController`
- `PartyMemberController`

### 4. 파티 멤버 운영

- 파티 나가기
- 멤버 강퇴
- 방장 위임
- 운영진 권한 관리
- 멤버 뮤트/해제
- 뮤트 상태에서 채팅/음성 제한

관련 컨트롤러:

- `PartyMemberController`
- `PartyMuteController`

### 5. 실시간 채팅

- 채팅 메시지 조회
- 채팅 읽음 상태 갱신
- STOMP 기반 실시간 채팅 송수신

관련 컨트롤러:

- `PartyChatController`
- `PartyChatSocketController`

### 6. 음성채널 상태 및 푸시 알림

- 파티 음성채널 접속자 조회
- 입장/퇴장 이벤트 처리
- 파티 토픽 브로드캐스트
- 사용자 전용 큐(`/user/queue/notifications`) 알림 전송

관련 컨트롤러:

- `PartyVoiceController`
- `PartyVoiceSocketController`

## 기술 스택

### Backend

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Persistence-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-010101?style=for-the-badge&logo=socketdotio&logoColor=white)
![SockJS](https://img.shields.io/badge/SockJS-Realtime-0F172A?style=for-the-badge)
![JWT](https://img.shields.io/badge/JWT-0.11.5-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![ModelMapper](https://img.shields.io/badge/ModelMapper-3.1.1-2563EB?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![H2](https://img.shields.io/badge/H2-Test%20Runtime-1E3A8A?style=for-the-badge)
![Gradle](https://img.shields.io/badge/Gradle-Multi%20Module-02303A?style=for-the-badge&logo=gradle&logoColor=white)

### Frontend

`front-end/package.json` 기준:

![React](https://img.shields.io/badge/React-19.1.1-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![React Router](https://img.shields.io/badge/React%20Router-7.9.5-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-7.1.7-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4.1.16-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-1.13.2-5A29E4?style=for-the-badge&logo=axios&logoColor=white)
![React Icons](https://img.shields.io/badge/React%20Icons-5.5.0-E91E63?style=for-the-badge)
![React Tooltip](https://img.shields.io/badge/React%20Tooltip-5.30.0-7C3AED?style=for-the-badge)
![SimpleBar](https://img.shields.io/badge/SimpleBar-6.3.3-0F172A?style=for-the-badge)

## 프로젝트 구조

```text
onion/
├── onion-domain/                  # Entity, Enum, Repository
├── onion-core/                    # DTO, Service, Security, Exception
├── onion-user-api/                # 사용자 REST API, WebSocket, 정적 리소스 서빙
├── onion-admin-api/               # 관리자 API 확장 모듈
├── front-end/                     # React + Vite 프론트엔드
├── postman/                       # Postman 컬렉션
├── build.gradle                   # 루트 Gradle 설정
└── settings.gradle                # 멀티 모듈 구성
```

## 모듈별 역할

### `onion-domain`

- 엔티티
- Enum
- Repository
- DB 드라이버 의존성(MySQL, H2)

### `onion-core`

- 서비스 로직
- DTO
- JWT 보안 구성
- 공통 예외 처리
- ModelMapper 기반 매핑

### `onion-user-api`

- 인증/친구/파티/채팅/음성 API
- WebSocket 설정
- 프론트엔드 빌드 결과 정적 리소스 통합

### `onion-admin-api`

- 관리자 API 모듈 시작점
- 향후 운영 기능 분리용 확장 지점

### `front-end`

- React 라우팅
- 레이아웃, 파티 카드, 친구 패널 UI
- Axios 기반 API 호출

## 실행 준비

### 백엔드 빌드 및 실행

```bash
./gradlew build
./gradlew :onion-user-api:bootRun
```

PowerShell:

```powershell
.\gradlew.bat build
.\gradlew.bat :onion-user-api:bootRun
```

### 프론트엔드 단독 실행

```bash
cd front-end
npm install
npm run dev
```

`onion-user-api`는 Gradle `node` 플러그인을 통해 `front-end`의 `npm run build`를 수행하고, 결과물을 `src/main/resources/static`으로 복사하는 구조입니다. 즉, 프론트 별도 개발 서버와 백엔드 통합 배포 두 방식을 모두 지원합니다.

## 주요 환경 변수

| 변수 | 설명 |
| --- | --- |
| `SERVER_PORT` | `onion-user-api` 실행 포트 |
| `DB_URL` | JDBC URL |
| `DB_DRIVER` | DB 드라이버 클래스명 |
| `DB_USERNAME` | DB 계정 |
| `DB_PASSWORD` | DB 비밀번호 |
| `JWT_SECRET` | JWT 서명 키 |
| `JWT_EXPIRATION` | JWT 만료 시간(ms) |

## 현재 코드 기준 참고 사항

- 실제 프로젝트 루트 이름은 디렉터리상 `gameHub-multi-module-`이지만, Gradle 루트 프로젝트명은 `onion`입니다.
- `onion-user-api`가 현재 서비스의 핵심 실행 모듈이며, `onion-admin-api`는 기본 시작점 수준으로 보입니다.
- 테스트 태스크는 각 모듈에서 `enabled = false`로 꺼져 있습니다.
- JPA 설정은 `ddl-auto: update`, `show-sql: true` 기준입니다.
- 프론트엔드 `dist` 산출물이 이미 `onion-user-api/src/main/resources/static` 아래에도 반영되어 있습니다.
- **멀티 모듈 백엔드와 React 프론트가 결합된 게임 파티 허브형 웹 서비스 모노레포**

## 보완한 부분

- 프론트 산출물 복사 경로를 `build`에서 `dist`로 정정
- 사용자 API 정적 리소스 산출물 추적 정책 정리
- 파티 상태 변경 시 잘못된 status 입력 `500` 오류를 `400` 처리로 보완
- 파티/친구/파티 쓰기 API 인증 정책 강화
- 파티 생성/참가 요청 입력값 검증 추가
- 참가 요청 중복 검사 로직을 존재 쿼리 기반으로 최적화
- WebSocket JWT 인증 인터셉터 및 인바운드 채널 보안 추가
- 실시간 채팅 + 읽음 상태 저장/조회 API 추가
- 음성채널 접속 상태 + 파티 브로드캐스트 + 사용자 푸시 알림 추가
- 권한 체계 확장(`LEADER` / `MANAGER` / `MEMBER`) 및 강퇴 정책 세분화
- 파티 뮤트/해제 기능 및 뮤트 시 채팅/음성 차단 적용
- 파티 초대코드 조회/재발급/입장 흐름 추가

## 관련 저장소

- 본 저장소는 `domain / core / user-api / admin-api / front-end`를 포함한 통합 모노레포입니다.
