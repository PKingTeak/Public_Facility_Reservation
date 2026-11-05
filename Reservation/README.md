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

### 중복 예약 방지 (동시성 제어)

**문제**: "같은 시간대 예약이 있는지 조회 → 없으면 저장"은 두 단계라서, 두 요청이 동시에 들어오면
둘 다 "예약 없음"을 보고 둘 다 저장할 수 있다 (경쟁 조건, race condition).

**해결**: 예약 트랜잭션 시작 시 해당 **시설 행을 비관적 락**(`SELECT ... FOR UPDATE`)으로 잡는다.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("select f from Facility f where f.id = :id")
Optional<Facility> findByIdWithLock(@Param("id") Long id);
```

같은 시설에 대한 예약은 락 때문에 한 번에 하나씩 처리되고, 뒤 요청은 앞 요청이 커밋한 예약을 보고 거절된다.

**왜 이 방법을 골랐나**

| 방법 | 장점 | 단점 | 판단 |
|---|---|---|---|
| 유니크 제약 | 가장 단순, DB가 보장 | 시간 "범위 겹침"(10\~12 vs 11\~13)은 유니크로 표현 불가 | ❌ |
| 낙관적 락 (`@Version`) | 락 대기 없음 | 충돌 시 재시도 로직 필요, 인기 시간대엔 실패가 잦음 | △ |
| **비관적 락 (시설 행)** | 범위 겹침도 확실히 막음, 구현 단순 | 같은 시설 예약끼리는 순서대로 처리 | ✅ 공공시설은 시설당 요청량이 많지 않아 대기 비용이 작음 |
| 분산 락 (Redis) | 서버 여러 대에서도 동작 | 인프라 추가 | 현재 규모엔 과함 |

**검증**: `ReservationConcurrencyTest` — 실제 DB(H2)에서 **10개 스레드가 동시에** 같은 시간대를 예약하면
**1건만 성공**하고 9건은 중복으로 거절되는지 확인한다.

---

## 🚀 실행 방법

### 사전 요구사항
- Java 25
- MySQL 실행 중 (테스트는 MySQL 없이 H2로 실행됨)

### 환경 변수 설정
DB 비밀번호·메일 계정은 코드에 쓰지 않고 환경 변수로 넣는다.

```powershell
# Windows PowerShell
$env:DB_PASSWORD="내 DB 비밀번호"
$env:MAIL_USERNAME="보내는 Gmail 주소"
$env:MAIL_PASSWORD="Gmail 앱 비밀번호"
```
`DB_URL`, `DB_USERNAME`은 기본값(`localhost:3306/reservation_db`, `root`)이 있어 필요할 때만 설정.

### 실행 / 테스트
```bash
cd Reservation/spring/reservation
./mvnw spring-boot:run   # 서버 실행
./mvnw test              # 전체 테스트 (MySQL 불필요)
```

---

## 🧪 테스트

| 테스트 | 종류 | 검증 내용 |
|---|---|---|
| `ReservationConcurrencyTest` | 통합 (H2 + 멀티스레드) | 동시 요청 10건 → 1건만 저장 |
| `ReservationServiceTest` | 단위 (Mockito) | 정상 예약, 겹침 거절, **맞닿은 시간 허용(10\~12 → 12\~14)**, 취소된 시간 재예약, 시작=종료 거절, 지난 날짜 거절, 타인 취소 거절, 중복 취소 거절, TimeSlot 겹침 경계값 |
| `EmailServiceTest` | 단위 (Mockito) | 정답 인증, 오답 시 실패 횟수 증가, **5회 실패 시 잠금**, 만료 코드 삭제, 재발급 시 초기화 |

---

## 🛠 트러블슈팅

### 1. 동시 예약 시 중복 저장 가능성
- **문제**: 조회 후 저장하는 구조라 동시 요청이 모두 통과할 수 있음
- **원인**: 중복 검사와 저장 사이에 다른 트랜잭션이 끼어들 수 있음
- **해결**: 시설 행 비관적 락으로 같은 시설 예약을 직렬화 (위 표 참고)
- **배운 점**: 단위 테스트(Mock)로는 동시성을 검증할 수 없어서, 실제 DB와 `CountDownLatch`로 스레드를 동시에 출발시키는 통합 테스트를 따로 작성했다

### 2. 이메일 인증 정보가 서버 재시작 시 사라짐
- **문제**: 인증번호를 `ConcurrentHashMap`(서버 메모리)에 저장 → 재시작하면 유실, 서버를 2대로 늘리면 코드를 받은 서버와 확인하는 서버가 달라 인증 실패
- **해결**: 이미 만들어 둔 `EmailVerification` 엔티티를 활용해 **DB에 저장** (이메일당 1건, 재발급 시 갱신)
- **추가 보완**
  - 6자리 코드는 100만 가지라 3분 안에 대입 공격이 가능 → **5회 실패 시 잠금**, 재발급 필요
  - 가입 완료 후 인증 정보 삭제 → 같은 인증으로 재가입 불가
  - 같은 이메일 중복 가입 차단
  - 콘솔에 인증번호를 출력하던 디버그 코드 제거
- **더 확장한다면**: 만료가 자동으로 처리되는 Redis(TTL) 사용

### 3. 잘못된 예약 시간이 500 에러로 응답됨
- **문제**: 시작 ≥ 종료 시간 요청 시 `IllegalStateException` → 처리되지 않아 **500 (서버 오류)**
- **원인**: 사용자 입력 오류와 서버 오류를 구분하지 않음
- **해결**: `InvalidRequestException`으로 바꿔 `GlobalExceptionHandler`가 **400**으로 응답. 지난 날짜 예약도 400으로 거절

### 4. API 응답에 비밀번호 해시 노출
- **문제**: `GET /users/{id}`가 `User` 엔티티를 그대로 반환 → JSON에 BCrypt 해시·이메일·권한 노출
- **해결**: 필요한 값만 담은 `UserResponse` DTO 반환
- **배운 점**: 엔티티를 직접 반환하면 필드가 추가될 때마다 의도치 않게 외부로 나갈 수 있다

### 5. 기타 수정
- DB 비밀번호가 `application.properties`에 하드코딩 → 환경 변수로 분리
- 관리자 회원 목록 API가 `/admin/admin/users`로 매핑돼 관리자 화면에서 목록이 안 뜨던 문제 수정
- `GET /reservation/AllData/{date}`에 `@PathVariable` 누락으로 날짜가 항상 null이던 문제 수정

---

## 🔍 배운 점 / 구현 포인트

- Spring Security의 `SecurityFilterChain`으로 URL별 접근 권한 세분화
- `@Embeddable` + `overlap()` 메서드로 타임슬롯 충돌 검증 로직 직접 구현
- JPA 비관적 락으로 동시성 문제 해결, **멀티스레드 통합 테스트로 실제 동작 검증**
- 이메일 인증을 메모리 → DB로 옮기며 서버 재시작·다중 서버 환경 고려
- 사용자 입력 오류(400)와 서버 오류(500) 구분, 엔티티 대신 DTO 응답
