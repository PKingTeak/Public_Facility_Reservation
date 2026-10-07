# 🏢 Public Facility Reservation System

공공시설 예약 관리 시스템 — 시설 예약, 사용자 인증, 관리자 권한 관리를 제공하는 Spring Boot 백엔드 프로젝트입니다.

---

## 📌 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 개발 기간 | 2026.08 |
| 개발 유형 | 개인 프로젝트 (포트폴리오) |
| 목적 | Spring Boot 기반 RESTful API 설계 및 보안 처리 학습 |

---

## ✅ 주요 기능

### 예약 관리
- 시설별 날짜 + 타임슬롯(시작시간~종료시간) 기반 예약
- 예약 상태 관리 (`RESERVED` / `CANCELLED`)
- 중복 예약 방지 — Lock 설정으로 동시성 제어
- 예약 취소 (중복 취소 방지 로직 포함)

### 사용자 인증 & 보안
- Spring Security 기반 인증/인가
- BCrypt 암호화 적용
- 이메일 인증 (회원가입 시 검증)
- CSRF 설정
- RBAC (역할 기반 접근 제어) — 일반 사용자 / 관리자

### 관리자 기능
- 사용자 역할 변경
- 관리자 전용 API 접근 권한 설정

### 데이터 검증
- DTO + Spring Validation으로 요청 데이터 검증
- TimeSlot 유효성 검증 (시작시간 < 종료시간 강제)

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 25 |
| Framework | Spring Boot 4.1.1 |
| Build Tool | Maven |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security, BCrypt |
| Validation | Spring Validation |
| Mail | Spring Mail |
| Test | JUnit, Mockito |

---

## 📁 프로젝트 구조

```
Reservation/
├── src/
│   └── App.java
└── spring/reservation/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/example/reservation/
    │   │   │   ├── Facilitys/
    │   │   │   │   ├── Controller/       # HTTP 요청 처리
    │   │   │   │   ├── Service/          # 비즈니스 로직
    │   │   │   │   ├── Repository/       # DB 접근 (JPA)
    │   │   │   │   ├── DTO/              # 요청/응답 데이터 객체
    │   │   │   │   ├── Security/         # Spring Security 설정
    │   │   │   │   ├── Authorization/    # 권한 관련 로직
    │   │   │   │   ├── Exception/        # 예외 처리
    │   │   │   │   ├── Facility.java     # 시설 Entity
    │   │   │   │   ├── Reservation.java  # 예약 Entity
    │   │   │   │   ├── User.java         # 사용자 Entity
    │   │   │   │   ├── UserDetail.java   # Spring Security UserDetails 구현
    │   │   │   │   └── EmailVerification.java  # 이메일 인증
    │   │   │   └── ReservationApplication.java  # 메인 클래스
    │   │   └── resources/
    │   │       ├── static/
    │   │       └── application.properties
    │   └── test/java/                    # JUnit 테스트 코드
    └── pom.xml
```

---

## ⚙️ 핵심 설계

### Reservation Entity
```java
// 타임슬롯 기반 예약 구조
@Embeddable
class TimeSlot {
    // 시작시간이 종료시간보다 이전이어야 한다는 검증 포함
    boolean overlap(TimeSlot other); // 중복 시간대 감지
}

// 예약 상태
enum Status { RESERVED, CANCELLED }
```

### 중복 예약 방지
- DB Lock 설정으로 동시에 같은 시간대에 예약 요청이 들어와도 하나만 처리
- JUnit + Mockito로 중복 예약 시나리오 테스트 작성

---

## 🚀 실행 방법

### 사전 요구사항
- Java 25
- MySQL 실행 중
- Maven

### 실행

```bash
# 프로젝트 루트에서
cd Reservation/spring/reservation

# application.properties에 DB 설정 입력 후
./mvnw spring-boot:run
```

### application.properties 설정 (예시)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reservation_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔍 배운 점 / 구현 포인트

- Spring Security의 `SecurityFilterChain`으로 URL별 접근 권한 세분화
- `@Embeddable` + `overlap()` 메서드로 타임슬롯 충돌 검증 로직 직접 구현
- JPA 비관적 락(Pessimistic Lock)으로 동시성 문제 해결
- 이메일 인증 플로우 직접 구현 (Spring Mail 활용)
