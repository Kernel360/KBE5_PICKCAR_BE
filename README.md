# PickCar - 차량 관제 서비스

## 🚗 프로젝트 소개

**PickCar**는 법인 차량 관리의 모든 과정을 한 번에 해결하는 차량 관제 서비스입니다.

차량 할당부터 실시간 운행 관제, 반납 후 운행일지 자동 기록까지 체계적으로 관리할 수 있으며,<br /> 사원에게 차량을 쉽게 배정하고 운행 중인 차량의 위치와 상태를 실시간으로 모니터링하여 차량 관리의 편의성을
제공합니다.

## ✨ 핵심 기능

### 👥 사용자 관리

- **회원가입 및 로그인**: 관리자와 사원으로 권한 분리
- **역할 기반 접근**: 회원 권한에 따른 다른 기능/화면 제공

### 🚙 차량 관리

- **차량 등록 및 관리**: 차량 등록 및 상태 변경
- **사원 차량 할당**: 사원에게 차량 할당 기능
- **할당 정보 조회**: 차량 할당 정보와 연관된 운행 기록 조회

### 📡 실시간 관제

- **실시간 위치 추적**: SSE를 통한 준실시간 차량 위치 추적
- **이동 경로 표현**: 발생 좌표 정보를 기반으로 한 준실시간 이동 경로 시각화
- **애뮬레이터**: RabbitMQ를 활용한 주기정보 및 이벤트 정보 전송

### 📊 운행일지 및 통계

- **자동 운행일지 생성**: ON → OFF 사이 주기정보 기반 운행 내역 자동 기록
- **상세 운행 정보**:
    - 운행 차량 정보, 운행 시작/종료 시간, 총 이동 거리, 상세 이동 경로

### 📈 대시보드

- **실시간 모니터링**: 차량 예약 및 상태 실시간 조회
- **통계 데이터**: 매일 새벽 전일 집계 데이터 수집 및 분석
    - 방문지별 통계, 일별 이동 거리 통계, 전일 기준 최다 이동 사원 통계

## 🏗 시스템 아키텍처
### 주요 구성 요소

- **웹 애플리케이션**: Spring Boot 기반 백엔드 서비스
- **메시지 큐**: RabbitMQ를 활용한 준실시간 데이터 처리
- **실시간 통신**: Redis + SSE를 통한 클라이언트-서버 간 준실시간 데이터 전송 및 추가 확장 고려
- **애뮬레이터**: 차량 위치 및 상태 정보 전송 ON/OFF 및 반납 처리

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

### 🎨 Frontend & Design
[FE Repository 보러가기](https://github.com/Kernel360/KBE5_PICKCAR_FE) <br/>
[![Cursor](https://custom-icon-badges.demolab.com/badge/Cursor-000000?logo=cursor-ai-white&style=for-the-badge)](#)
[![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white&style=for-the-badge)](#)
[![TailwindCSS](https://img.shields.io/badge/TailwindCSS-06B6D4?logo=tailwindcss&logoColor=white&style=for-the-badge)](#)
[![DaisyUI](https://img.shields.io/badge/DaisyUI-5A0EF8?logo=daisyui&logoColor=white&style=for-the-badge)](#)
[![Chart.js](https://img.shields.io/badge/Chart.js-FF6384?logo=chartdotjs&logoColor=white&style=for-the-badge)](#)
[![Vite](https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=white&style=for-the-badge)](#)

## 👥 팀 정보

|                             Backend(팀장)                              |                                Backend                                 |                               Backend                                |       
|:--------------------------------------------------------------------:|:----------------------------------------------------------------------:|:--------------------------------------------------------------------:|
| <img src="https://github.com/SeoMoonk.png" width="120" height="120"> | <img src="https://github.com/BaileyPark.png" width="120" height="120"> | <img src="https://github.com/sseung-g.png" width="120" height="120"> |
|                  [김성훈](https://github.com/SeoMoonk)                  |                  [박영제](https://github.com/BaileyPark)                  |                  [이승경](https://github.com/sseung-g)                  |
