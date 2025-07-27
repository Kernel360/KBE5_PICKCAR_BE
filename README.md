# PickCar - 차량 관제 서비스

## 🚗 프로젝트 소개

**PickCar**는 법인 차량 관리의 모든 과정을 한 번에 해결하는 차량 관제 서비스입니다.

차량 할당부터 실시간 운행 관제, 반납 후 운행일지 자동 기록까지 체계적으로 관리할 수 있으며,<br /> 사원에게 차량을 쉽게 배정하고 운행 중인 차량의 위치와 상태를 실시간으로 모니터링하여 차량 관리의 편의성을
제공합니다.

## 📕 최종 발표 자료 보기
[Pickcar_최종발표.pdf](https://github.com/user-attachments/files/21454868/Pickcar_.pdf)

## ✨ 핵심 기능

### 👥 사용자 관리

- **회원가입 및 로그인**: 관리자와 사원으로 권한 분리
- **역할 기반 접근**: 회원 권한에 따른 다른 기능/화면 제공

<table align="center">
  <tr>
    <th><code>온보딩 페이지</code></th>
    <th><code>회원가입</code></th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/ab3438e0-aeaa-44b8-8e63-4a314e355f93" width="300" alt="온보딩 페이지"></td>
    <td><img src="https://github.com/user-attachments/assets/4d178199-19a8-437b-a504-b10d59b3dcca" width="180" alt="회원가입"></td>
  </tr>
</table>

### 🚙 차량 관리

- **차량 등록 및 관리**: 차량 등록 및 상태 변경
- **사원 차량 할당**: 사원에게 차량 할당 기능
- **할당 정보 조회**: 차량 할당 정보와 연관된 운행 기록 조회

<table align="center">
  <tr>
    <th><code>차량 리스트</code></th>
    <th><code>차량 등록</code></th>
    <th><code>차량 상태 변경</code></th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/0d26896a-6827-42ec-b36e-6198d579af5a" width="300" alt="차량 리스트"></td>
    <td><img src="https://github.com/user-attachments/assets/4d178199-19a8-437b-a504-b10d59b3dcca" width="180" alt="차량 등록"></td>
    <td><img src="https://github.com/user-attachments/assets/efb2a574-6efe-4a74-a007-1d071a25b30d" width="180" alt="차량 상태 변경"></td>
  </tr>
</table>

<table align="center">
  <tr>
    <th><code>사원 차량 관리</code></th>
    <th><code>차량 할당</code></th>
    <th><code>상세 할당 정보</code></th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/1caf705b-a634-43a5-aaa0-1f52d47b01f2" width="300" alt="사원 차량 관리"></td>
    <td><img src="https://github.com/user-attachments/assets/41e13de2-f236-4447-81a6-55f0f91ecc29" width="180" alt="차량 할당"></td>
    <td><img src="https://github.com/user-attachments/assets/7e846d0c-042b-45ff-a2d5-606dfc816a38" width="180" alt="상세 할당 정보"></td>
  </tr>
</table>

### 📡 실시간 관제

- **실시간 위치 추적**: SSE를 통한 준실시간 차량 위치 추적
- **이동 경로 표현**: 발생 좌표 정보를 기반으로 한 준실시간 이동 경로 시각화
- **애뮬레이터**: RabbitMQ를 활용한 주기정보 및 이벤트 정보 전송

<table align="center">
  <tr>
    <th><code>실시간 관제</code></th>
    <th><code>차량 애뮬레이터</code></th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/c974ee0d-2d8f-456c-89ea-f4a12bf1174c" width="300" alt="실시간 관제"></td>
    <td><img src="https://github.com/user-attachments/assets/346224e9-77bc-4b83-863e-c4ab42263017" width="180" alt="차량 애뮬레이터"></td>
  </tr>
</table>

### 📊 운행일지 및 통계

- **자동 운행일지 생성**: ON → OFF 사이 주기정보 기반 운행 내역 자동 기록
- **상세 운행 정보**:
    - 운행 차량 정보, 운행 시작/종료 시간, 총 이동 거리, 상세 이동 경로
- **실시간 모니터링**: 차량 예약 및 상태 실시간 조회
- **통계 데이터**: 매일 새벽 전일 집계 데이터 수집 및 분석
    - 방문지별 통계, 일별 이동 거리 통계, 전일 기준 최다 이동 사원 통계

<table align="center">
  <tr>
    <th><code>운행일지 리스트 조회</code></th>
    <th><code>운행일지 상세보기</code></th>
    <th><code>통계 대시보드</code></th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d55a2d82-0026-417d-a286-0b10dcad1de5" width="300" alt="운행일지 리스트 조회"></td>
    <td><img src="https://github.com/user-attachments/assets/ab93ae4b-f972-4c86-a6b5-7189126721ab" width="180" alt="운행일지 상세보기"></td>
    <td><img src="https://github.com/user-attachments/assets/05e3c74e-3dab-48c2-9543-497329fd7b57" width="300" alt="통계 대시보드"></td>
  </tr>
</table>

## 🏗 시스템 아키텍처
<img width="3093" height="2225" alt="Pickcar_architecture" src="https://github.com/user-attachments/assets/308cee8e-9428-442e-828c-1d291138944c" />

## 프로젝트 구조

### Multi-Module
```
├── common/      # 공통 모듈
├── database/    # 데이터베이스 모듈
├── domain/      # 비즈니스 로직 모듈
├── producer/    # RabbitMQ 프로듀서 모듈
├── consumer/    # RabbitMQ 컨슈머 모듈
└── emulator/    # SSE 모듈
```

### Package 구성
```
└── Base Domain
    ├── application     # Application 서비스 레이어 (Application Service ...)
    ├── infrastructure  # 외부 시스템 연동 및 구현체 (JPA Repository, Projection DTO ...)
    ├── presentation    # 컨트롤러 및 외부 인터페이스 (REST Controller, Request/Response DTO ...)
    └── domain          # 도메인 모델 및 비즈니스 규칙 (Entity, VO ...)
```

## 🛠 기술 스택

### ⚙ Backend
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white&labelColor=ED8B00&style=for-the-badge)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?logo=spring-boot&logoColor=white&labelColor=6DB33F&style=for-the-badge)](#)
[![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white&labelColor=6DB33F&style=for-the-badge)](#)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white&style=for-the-badge)](#)
[![JWT](https://img.shields.io/badge/JWT-0.11.5-000000?logo=jsonwebtokens&logoColor=white&labelColor=000000&style=for-the-badge)](#)
[![Swagger](https://img.shields.io/badge/Swagger-2.8.6-85EA2D?logo=swagger&logoColor=black&labelColor=85EA2D&style=for-the-badge)](#)

### 🗄️ Database & Message Queue
[![MySQL](https://img.shields.io/badge/MySQL-8.0.33-4479A1?logo=mysql&logoColor=white&labelColor=4479A1&style=for-the-badge)](#)
[![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white&style=for-the-badge)](#)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?logo=rabbitmq&logoColor=white&style=for-the-badge)](#)

### 🏗️ Build & DevOps
[![Gradle](https://img.shields.io/badge/Gradle-8.13-02303A?logo=gradle&logoColor=white&labelColor=02303A&style=for-the-badge)](#)
[![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white&style=for-the-badge)](#)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2496ED?logo=docker&logoColor=white&style=for-the-badge)](#)
[![Linux](https://img.shields.io/badge/Linux-FCC624?logo=linux&logoColor=black&style=for-the-badge)](#)

### 📊 Monitoring & Logging
[![Promtail](https://img.shields.io/badge/Promtail-2.9.0-F46800?logo=grafana&logoColor=white&labelColor=F46800&style=for-the-badge)](#)
[![Loki](https://img.shields.io/badge/Loki-2.9.14-F46800?logo=grafana&logoColor=white&labelColor=F46800&style=for-the-badge)](#)
[![Grafana](https://img.shields.io/badge/Grafana-10.0.0-F46800?logo=grafana&logoColor=white&labelColor=F46800&style=for-the-badge)](#)
[![Pinpoint](https://img.shields.io/badge/Pinpoint-2.5.4-22aef4?logoColor=white&labelColor=22aef4&style=for-the-badge)](#)

### 🧪 Performance Testing
[![K6](https://img.shields.io/badge/K6-7D64FF?logo=k6&logoColor=white&style=for-the-badge)](#)
[![nGrinder](https://img.shields.io/badge/nGrinder-4CAF50?logo=naver&logoColor=white&style=for-the-badge)](#)

### 🎨 Frontend 보러가기
[FE Repository 보러가기](https://github.com/Kernel360/KBE5_PICKCAR_FE) <br/>

## 👥 팀 정보

|                             Backend(팀장)                              |                                Backend                                 |                               Backend                                |       
|:--------------------------------------------------------------------:|:----------------------------------------------------------------------:|:--------------------------------------------------------------------:|
| <img src="https://github.com/SeoMoonk.png" width="120" height="120"> | <img src="https://github.com/BaileyPark.png" width="120" height="120"> | <img src="https://github.com/sseung-g.png" width="120" height="120"> |
|                  [김성훈](https://github.com/SeoMoonk)                  |                  [박영제](https://github.com/BaileyPark)                  |                  [이승경](https://github.com/sseung-g)                  |
