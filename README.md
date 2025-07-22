# PickCar - 차량 관제 서비스

## 🚗 프로젝트 소개

**PickCar**는 법인 차량 관리의 모든 과정을 한 번에 해결하는 차량 관제 서비스입니다.

차량 할당부터 실시간 운행 관제, 반납 후 운행일지 자동 기록까지 체계적으로 관리할 수 있으며,<br /> 사원에게 차량을 쉽게 배정하고 운행 중인 차량의 위치와 상태를 실시간으로 모니터링하여 차량 관리의 편의성을 제공합니다.

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

## 🚀 향후 개선 방향

### 목표 1: 인프라 현대화
- 다수의 EC2 서버를 ECS/EKS로 전환하여 컨테이너 기반 관리 체계 구축

### 목표 2: 안정성 향상
- DLQ(Dead Letter Queue) Fallback 개념 적용으로 메시지 처리 안정성 강화

### 목표 3: 서비스 고도화
- 기존 기능의 고도화
- 체계적인 부하 테스트 및 모니터링 시스템 구축

## 프로젝트 구조

```
├── common/      # 공통 모듈
├── database/    # 데이터베이스 모듈
├── domain/      # 비즈니스로직 모듈
├── producer/    # RabbitMQ 프로듀서 모듈
├── consumer/    # RabbitMQ 컨슈머 모듈
└── emulator/    # SSE 모듈
```


## 🛠 기술 스택

**백엔드**

Framework: Spring Boot<br />
ORM: JPA (Java Persistence API)<br />
Database: MySQL, Redis<br />
Build Tool: Gradle

**인프라**

Containerization: Docker<br />
Version Control: GitHub<br />
Cloud Platform: AWS<br />
Message Queue: RabbitMQ

**프론트**

Language: TypeScript<br />
Styling: Tailwind CSS<br />
AI: Cursor

**모니터링**

APM: Pinpoint<br />
Log Collector: Promtail, Loki<br />
Visualization: Grafana<br />
Load Testing: K6, nGrinder

**외부 서비스**

Map API: Kakao Map API

## 👥 팀 정보
|                            Backend                          |                                Backend                                |                                Backend                                |       
|:------------------------------------------------------------------------:|:---------------------------------------------------------------------:|:---------------------------------------------------------------------:|
| <img src="https://github.com/SeoMoonk.png" width="120" height="120"> | <img src="https://github.com/BaileyPark.png" width="120" height="120"> | <img src="https://github.com/sseung-g.png" width="120" height="120"> |
|                  [김성훈](https://github.com/SeoMoonk)                  |                  [박영제](https://github.com/BaileyPark)                   |                  [이승경](https://github.com/sseung-g)                   |


---

*더 자세한 정보나 문의사항이 있으시면 언제든 연락해 주세요.*
