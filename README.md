# SpringStudy

## 프로젝트 소개

SpringStudy는 Java와 Spring Boot를 기반으로 백엔드 개발의
핵심 기술을 학습하고 실습하기 위해 개발한 REST API 프로젝트입니다.

사용자 및 주문 관리 기능을 중심으로 데이터베이스 연동,
인증 및 인가, 예외 처리, 트랜잭션, 테스트 코드 작성,
데이터베이스 마이그레이션을 구현하였습니다.

단순한 CRUD 구현을 넘어 데이터 무결성, 보안,
유지보수성을 고려한 백엔드 애플리케이션 개발을 목표로 합니다.

## 기술 스택

### Backend
- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Hibernate
- Bean Validation

### Security
- Spring Security
- JWT (JJWT 0.12.6)
- BCrypt

### Database
- PostgreSQL 17
- Flyway

### Test
- JUnit 5
- Mockito
- MockMvc
- Spring Security Test

### Build & Version Control
- Gradle
- Git
- GitHub

### Development Tools
- IntelliJ IDEA
- Postman
- pgAdmin

## 프로젝트 구조

```text
springstudy
├── src
│   ├── main
│   │   ├── java/com/study/springstudy
│   │   │   ├── controller
│   │   │   ├── domain
│   │   │   ├── dto
│   │   │   ├── exception
│   │   │   ├── global
│   │   │   │   ├── config
│   │   │   │   ├── jwt
│   │   │   │   └── security
│   │   │   ├── repository
│   │   │   ├── service
│   │   │   └── SpringstudyApplication.java
│   │   └── resources
│   │       ├── db/migration
│   │       ├── application.properties
│   │       └── application-flywaytest.properties
│   └── test
│       └── java/com/study/springstudy
│           ├── controller
│           ├── global/jwt
│           ├── repository
│           ├── security
│           └── service
├── build.gradle
├── settings.gradle
└── README.md
```

### 패키지별 역할

| 패키지 | 역할 |
|---|---|
| controller | HTTP 요청 및 응답 처리 |
| service | 비즈니스 로직 및 트랜잭션 처리 |
| repository | Spring Data JPA 기반 데이터 접근 |
| domain | Entity 및 도메인 모델 관리 |
| dto | 요청 및 응답 데이터 전달 |
| exception | 커스텀 예외 및 공통 예외 처리 |
| global/config | Spring Security 설정 관리 |
| global/jwt | JWT 발급, 검증 및 인증 필터 |
| global/security | 공통 보안 오류 응답 처리 |
| db/migration | Flyway 기반 데이터베이스 마이그레이션 |
| test | 단위 테스트 및 통합 테스트 |

## 주요 기능

### 1. 사용자 관리
- 사용자 등록, 조회, 수정 및 삭제
- 사용자 검색 및 페이징
- DTO를 활용한 요청 및 응답 데이터 분리
- Bean Validation을 활용한 입력값 검증

### 2. 주문 관리
- 사용자별 주문 등록, 조회 및 삭제
- JPA 일대다 및 다대일 연관관계 매핑
- Fetch Join을 활용한 연관 데이터 조회
- Cascade 및 orphanRemoval을 활용한 연관관계 관리

### 3. 인증 및 인가
- 회원가입 및 로그인
- BCrypt 기반 비밀번호 해싱
- JWT 발급 및 유효성 검증
- Spring Security 기반 인증 처리
- ROLE_USER 및 ROLE_ADMIN 기반 접근 권한 관리
- 사용자 및 주문 소유권 검증

### 4. 예외 처리 및 데이터 무결성
- 커스텀 예외 클래스 구현
- GlobalExceptionHandler 기반 공통 예외 처리
- HTTP 상태 코드에 따른 오류 응답
- 인증 및 인가 실패 응답 형식 통일
- 이메일 중복 검사 및 UNIQUE 제약 조건 적용
- 실제 데이터베이스 무결성 예외 처리

### 5. 데이터베이스 마이그레이션
- Flyway 기반 데이터베이스 스키마 버전 관리
- V1: 초기 데이터베이스 스키마 구성
- V2: 사용자 생성 시각 컬럼 추가
- V3: 이메일 UNIQUE 제약 조건 이름 변경
- 테스트 데이터베이스를 활용한 마이그레이션 검증

### 6. 테스트
- JUnit 및 Mockito 기반 단위 테스트
- MockMvc 기반 Controller 테스트
- JWT 및 Spring Security 테스트
- 실제 PostgreSQL 연동 통합 테스트
- 정상 동작 및 예외 상황 검증

## API 명세

### 기본 정보

- Base URL: http://localhost:8080
- 인증 방식: JWT Bearer Token
- 요청 및 응답 형식: JSON (일부 테스트 API는 문자열 응답)
- 인증이 필요한 API는 Authorization 헤더에 JWT를 전달해야 합니다.

### 인증 API

| Method | Endpoint | 기능 | 접근 조건 | 성공 상태 코드 |
|---|---|---|---|---|
| POST | /auth/register | 회원가입 | 공개 | 201 |
| POST | /auth/login | 로그인 | 공개 | 200 |

### 사용자 API

| Method | Endpoint | 기능 | 접근 조건 | 성공 상태 코드 |
|---|---|---|---|---|
| GET | /users | 전체 사용자 조회 | 공개 | 200 |
| GET | /users?page=&size= | 페이징 조회 | 공개 | 200 |
| GET | /users?name= | 이름 검색 | 공개 | 200 |
| GET | /users?minAge= | 최소 나이 검색 | 공개 | 200 |
| GET | /users?name=&minAge= | 복합 조건 검색 | 공개 | 200 |
| GET | /users/{id} | 사용자 단건 조회 | 인증 | 200 |
| POST | /users | 사용자 생성 | 인증 | 201 |
| PATCH | /users/{id} | 사용자 수정 | 본인 | 200 |
| DELETE | /users/{id} | 사용자 삭제 | 본인 | 204 |

### 주문 API

| Method | Endpoint | 기능 | 접근 조건 | 성공 상태 코드 |
|---|---|---|---|---|
| GET | /orders | 전체 주문 조회 | 인증 | 200 |
| GET | /orders/{id} | 주문 단건 조회 | 인증 | 200 |
| POST | /orders | 주문 생성 | 인증 | 201 |
| GET | /users/{id}/orders | 사용자별 주문 조회 | 본인 | 200 |
| DELETE | /users/{userId}/orders/{orderId} | 사용자 주문 삭제 | 본인 및 주문 소유권 | 204 |

### 관리자 및 학습용 API

| Method | Endpoint | 기능 | 접근 조건 | 성공 상태 코드 |
|---|---|---|---|---|
| GET | /admin/test | 관리자 접근 테스트 | ADMIN | 200 |
| GET | /hello | 기본 응답 테스트 | 인증 | 200 |
| GET | /products?category=&maxPrice= | 요청 파라미터 실습 | 인증 | 200 |

### 주요 API 요청 및 응답 예시

#### 1. 회원가입

- Method: POST
- Endpoint: /auth/register
- 인증: 불필요

Request Body

```json
{
  "name": "홍길동",
  "age": 25,
  "email": "user@example.com",
  "password": "examplePassword123"
}
```

입력 조건

- name: 필수, 공백 불가
- age: 필수, 0 이상
- email: 필수, 올바른 이메일 형식
- password: 필수, 최소 8자 이상

Response: 201 Created

응답 본문 없음.

#### 2. 로그인

- Method: POST
- Endpoint: /auth/login
- 인증: 불필요

Request Body

```json
{
  "email": "user@example.com",
  "password": "examplePassword123"
}
```

Response: 200 OK

```json
{
  "accessToken": "example-jwt-token"
}
```

#### 3. JWT 인증 요청

인증이 필요한 API를 호출할 때는 발급받은 JWT를
Authorization 헤더에 포함해야 합니다.

```http
Authorization: Bearer <accessToken>
```

#### 4. 사용자 생성

- Method: POST
- Endpoint: /users
- 인증: 필요

Request Body

```json
{
  "name": "홍길동",
  "age": 25
}
```

Response: 201 Created

```json
{
  "id": 1,
  "name": "홍길동",
  "age": 25
}
```

#### 5. 사용자 단건 조회

- Method: GET
- Endpoint: /users/1
- 인증: 필요

Response: 200 OK

```json
{
  "id": 1,
  "name": "홍길동",
  "age": 25
}
```

#### 6. 사용자 정보 수정

- Method: PATCH
- Endpoint: /users/1
- 인증: 필요
- 접근 조건: 본인

Request Body

```json
{
  "age": 26
}
```

Response: 200 OK

```json
{
  "id": 1,
  "name": "홍길동",
  "age": 26
}
```

#### 7. 사용자 검색 및 페이징

```http
GET /users?name=홍길동
GET /users?minAge=20
GET /users?name=홍길동&minAge=20
GET /users?page=0&size=10
```

- 검색 API는 `List<UserResponse>`를 반환합니다.
- 페이징 API는 `Page<UserResponse>`를 반환합니다.
- 인증 없이 접근할 수 있습니다.

#### 8. 주문 생성

- Method: POST
- Endpoint: /orders
- 인증: 필요

Request Body

```json
{
  "userId": 1,
  "productName": "Keyboard",
  "price": 50000
}
```

Response: 201 Created

```json
{
  "id": 10,
  "productName": "Keyboard",
  "price": 50000,
  "userId": 1,
  "userName": "홍길동"
}
```

#### 9. 사용자별 주문 조회

- Method: GET
- Endpoint: /users/1/orders
- 인증: 필요
- 접근 조건: 본인

Response: 200 OK

```json
[
  {
    "id": 10,
    "productName": "Keyboard",
    "price": 50000,
    "userId": 1,
    "userName": "홍길동"
  }
]
```

#### 10. 사용자 주문 삭제

- Method: DELETE
- Endpoint: /users/1/orders/10
- 인증: 필요
- 접근 조건: 본인 및 주문 소유권

Response: 204 No Content

응답 본문 없음.

#### 11. 사용자 삭제

- Method: DELETE
- Endpoint: /users/1
- 인증: 필요
- 접근 조건: 본인

Response: 204 No Content

응답 본문 없음.

#### 12. 관리자 접근 테스트

- Method: GET
- Endpoint: /admin/test
- 인증: 필요
- 접근 조건: ROLE_ADMIN

Response: 200 OK

```text
관리자 접근 성공
```

### 공통 오류 응답

GlobalExceptionHandler와 SecurityErrorResponseWriter를
활용하여 일관된 오류 응답 형식을 제공합니다.

| 필드 | 타입 | 설명 |
|---|---|---|
| status | Integer | HTTP 상태 코드 |
| message | String | 오류 메시지 |
| errors | Object | 필드별 입력값 검증 오류 |

errors 필드는 null인 경우 JSON 응답에서 생략됩니다.

#### 주요 오류 상태 코드

| 상태 코드 | 의미 | 발생 상황 |
|---|---|---|
| 400 | Bad Request | 입력값 검증 실패 |
| 401 | Unauthorized | 인증 정보 없음, JWT 검증 실패, 로그인 실패 |
| 403 | Forbidden | 접근 권한 부족, 사용자 및 주문 소유권 위반 |
| 404 | Not Found | 사용자 또는 주문이 존재하지 않음 |
| 409 | Conflict | 이메일 중복 |
| 500 | Internal Server Error | 서버 내부 오류 |

#### 400 Bad Request

```json
{
  "status": 400,
  "message": "입력값이 올바르지 않습니다.",
  "errors": {
    "name": "이름은 필수입니다."
  }
}
```

#### 401 Unauthorized

인증 정보가 없는 경우

```json
{
  "status": 401,
  "message": "인증이 필요합니다."
}
```

유효하지 않은 JWT를 전달한 경우

```json
{
  "status": 401,
  "message": "유효하지 않은 인증 토큰입니다."
}
```

#### 403 Forbidden

접근 권한이 부족한 경우

```json
{
  "status": 403,
  "message": "접근 권한이 없습니다."
}
```

#### 오류 응답 처리 정책

- 입력값 검증 실패 시 필드별 오류 메시지를 반환합니다.
- 인증 실패와 인가 실패를 401 및 403으로 구분합니다.
- 이메일 중복 발생 시 409 Conflict를 반환합니다.
- 실제 데이터베이스의 이메일 UNIQUE 제약 조건 위반도 처리합니다.
- 서버 내부의 예외 정보 및 스택 트레이스 노출을 제한합니다.

## 실행 방법

### 1. 사전 요구사항

- Java 21 (JDK)
- PostgreSQL 17
- Git
- 인터넷 연결 (최초 Gradle 및 의존성 다운로드)

Gradle Wrapper를 사용하므로 Gradle을 별도로 설치할
필요는 없습니다.

### 2. 프로젝트 다운로드

```bash
git clone https://github.com/ingwoovy/spring-study.git springstudy
cd springstudy
```

### 3. 데이터베이스 생성

PostgreSQL 서버를 실행한 후 다음 데이터베이스를 생성합니다.

```sql
CREATE DATABASE study_db;
CREATE DATABASE springstudy_flyway_test;
```

- study_db: 애플리케이션 실행용 데이터베이스
- springstudy_flyway_test: 통합 테스트용 데이터베이스

테이블 및 스키마는 Flyway를 통해 자동으로 생성되므로
직접 생성할 필요가 없습니다.

### 4. 애플리케이션 설정

보안을 위해 application.properties 파일은
Git 추적 대상에서 제외하도록 설정되어 있습니다.

다음 경로에 application.properties 파일을 생성합니다.

```text
src/main/resources/application.properties
```

설정 예시

```properties
spring.application.name=springstudy

spring.datasource.url=jdbc:postgresql://localhost:5432/study_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=false
spring.flyway.baseline-version=1

jwt.secret=${JWT_SECRET}
jwt.expiration=3600000

spring.web.error.include-message=never
spring.web.error.include-stacktrace=never
spring.web.error.include-binding-errors=never
spring.web.error.include-exception=false
```

PostgreSQL 사용자 이름과 접속 정보는
각자의 개발 환경에 맞게 설정합니다.

### 5. 환경변수 설정

Windows PowerShell 기준

#### 데이터베이스 비밀번호 설정

```powershell
$env:DB_PASSWORD="your-db-password"
```

your-db-password에는 실제 PostgreSQL 접속 비밀번호를 입력합니다.

#### JWT 비밀키 생성 및 설정

SpringStudy는 HS256 알고리즘을 사용하여
JWT를 서명하고 검증합니다.

JWT 비밀키는 최소 256비트(32바이트)의
Base64 인코딩된 키를 사용해야 합니다.

다음 PowerShell 명령어를 통해 안전한 난수 기반의
JWT 비밀키를 생성하고 환경변수에 저장할 수 있습니다.

```powershell
$key = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($key)
$env:JWT_SECRET = [Convert]::ToBase64String($key)
$rng.Dispose()
```

- DB_PASSWORD: PostgreSQL 접속 비밀번호
- JWT_SECRET: JWT 서명 및 검증에 사용하는 비밀키

환경변수를 설정한 터미널에서 애플리케이션을 실행합니다.

JWT 비밀키가 변경되면 기존에 발급된 JWT는
새로운 비밀키로 검증할 수 없습니다.

실제 비밀번호와 JWT 비밀키는 GitHub에 업로드하지 않습니다.

### 6. 애플리케이션 실행

환경변수를 설정한 터미널에서 다음 명령어를 실행합니다.

Windows

```powershell
.\gradlew.bat bootRun
```

macOS / Linux

```bash
./gradlew bootRun
```

애플리케이션 실행 시 Flyway가 데이터베이스의
마이그레이션 이력을 확인하고 미적용 버전을 실행합니다.

Hibernate는 데이터베이스 스키마와 Entity의
매핑이 일치하는지 검증합니다.

### 7. 실행 확인

기본 접속 주소

```text
http://localhost:8080
```

Postman을 사용하여 다음 API를 호출할 수 있습니다.

```http
GET /users
```

회원가입 및 로그인 API를 통해 JWT를 발급받은 후
인증이 필요한 API를 호출할 수 있습니다.

### 8. 테스트 데이터베이스 설정

테스트용 설정 파일 경로

```text
src/main/resources/application-flywaytest.properties
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/springstudy_flyway_test

spring.jpa.hibernate.ddl-auto=validate

spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=false
```

flywaytest 프로필을 사용하는 DB 연동 테스트는
별도의 테스트 데이터베이스에서 실행됩니다.

## 테스트

### 테스트 환경

- JUnit 5
- Mockito
- MockMvc
- Spring Security Test
- PostgreSQL 17
- Flyway

### 테스트 구성

| 테스트 유형 | 검증 내용 |
|---|---|
| Service 단위 테스트 | 비즈니스 로직 및 예외 처리 |
| Controller 테스트 | HTTP 요청, 응답 및 상태 코드 |
| JWT 테스트 | 토큰 발급 및 검증 |
| PasswordEncoder 테스트 | 비밀번호 암호화 및 일치 여부 |
| Security 통합 테스트 | 인증 및 역할 기반 인가 |
| 데이터베이스 통합 테스트 | 데이터 무결성 및 실제 DB 연동 |
| Flyway 연동 테스트 | 마이그레이션 및 생성 시각 관리 |

### 테스트 실행 방법

데이터베이스 및 환경변수 설정을 완료한 후
다음 명령어를 실행합니다.

Windows

```powershell
.\gradlew.bat clean test
```

macOS / Linux

```bash
./gradlew clean test
```

### 테스트 결과 확인

Gradle 테스트 결과는 다음 경로에서 확인할 수 있습니다.

```text
build/reports/tests/test/index.html
```

### 테스트 환경 분리

flywaytest 프로필을 사용하는 데이터베이스 통합 테스트는
springstudy_flyway_test 데이터베이스에서 실행됩니다.

Flyway를 통해 데이터베이스 스키마를 관리하고,
실제 PostgreSQL 환경에서 데이터 무결성 및
Entity 매핑을 검증합니다.

### 전체 테스트 실행 결과

- 검증 일자: 2026-11-25
- 전체 테스트 클래스: 11개
- 전체 테스트: 39개
- 성공: 39개
- 실패: 0개
- 건너뛴 테스트: 0개
- 테스트 성공률: 100%
- 빌드 결과: BUILD SUCCESSFUL

| 테스트 클래스 | 테스트 개수 | 결과 |
|---|---:|---|
| BasicTest | 1 | PASS |
| AuthControllerTest | 1 | PASS |
| UserControllerTest | 6 | PASS |
| JwtUtilTest | 4 | PASS |
| UserCreatedAtIntegrationTest | 1 | PASS |
| PasswordEncoderTest | 1 | PASS |
| SecurityIntegrationTest | 7 | PASS |
| AuthServiceIntegrationTest | 1 | PASS |
| AuthServiceTest | 7 | PASS |
| UserServiceTest | 9 | PASS |
| SpringstudyApplicationTests | 1 | PASS |
| 합계 | 39 | PASS |

단위 테스트와 통합 테스트를 통해 주요 비즈니스 로직,
HTTP 요청 및 응답, 인증 및 인가, 데이터 무결성,
데이터베이스 마이그레이션 동작을 검증하였습니다.

## 기술적 문제 해결 경험 (Troubleshooting)

### 1. 이메일 중복 검사 및 데이터 무결성 보장

#### Problem
Service 계층에서 이메일 중복 검사를 수행하더라도
동시 요청이 발생하면 중복 검사를 통과할 가능성이
존재하였습니다.

#### Cause
이메일 존재 여부 확인과 실제 데이터 저장은
서로 다른 작업이므로 애플리케이션의 사전 검사만으로는
데이터 무결성을 완전히 보장할 수 없습니다.

#### Solution
- existsByEmail을 활용한 이메일 사전 중복 검사
- PostgreSQL의 UNIQUE 제약 조건 적용
- saveAndFlush를 활용한 데이터베이스 제약 조건 위반 확인
- 이메일 중복 예외를 DuplicateEmailException으로 변환
- HTTP 409 Conflict 응답 처리

#### Result
애플리케이션과 데이터베이스 계층에서 이메일 중복을
검사하도록 구성하였습니다.

실제 PostgreSQL 연동 테스트를 통해 이메일 UNIQUE
제약 조건 위반에 대한 예외 처리 동작을 검증하였습니다.

### 2. Hibernate 제약 조건 이름 조회 문제

#### Problem
데이터베이스 제약 조건 위반 발생 시 Hibernate의
getConstraintName()이 null을 반환하는 문제가
발생하였습니다.

#### Cause
데이터베이스 오류 메시지로부터 제약 조건 이름을
추출하는 방식은 오류 메시지 형식 및 언어에 따라
정상적으로 동작하지 않을 수 있습니다.

#### Solution
- 예외의 원인 체인 탐색
- PostgreSQL JDBC의 PSQLException 확인
- SQLSTATE 23505를 통한 UNIQUE 제약 조건 위반 확인
- getServerErrorMessage().getConstraint()를 활용한
  제약 조건 이름 확인
- uk_users_email 제약 조건 위반 여부 확인
- 다른 데이터 무결성 예외는 기존 예외로 다시 전달

#### Result
이메일 UNIQUE 제약 조건 위반을 다른 데이터 무결성
예외와 구분하여 처리하도록 개선하였습니다.

실제 PostgreSQL 연동 테스트를 통해 예외 변환
동작을 검증하였습니다.

### 3. 인증 및 인가 실패 응답 형식 통일

#### Problem
Spring MVC와 Spring Security에서 발생하는 오류의
응답 형식이 일관되지 않는 문제가 있었습니다.

#### Cause
GlobalExceptionHandler는 Spring MVC의 예외를 처리하며,
Spring Security 필터 체인에서 발생하는 인증 및 인가
오류는 별도의 처리 방식이 필요합니다.

#### Solution
- SecurityErrorResponseWriter 구현
- AuthenticationEntryPoint를 통한 401 응답 처리
- AccessDeniedHandler를 통한 403 응답 처리
- JWT 검증 실패 시 공통 오류 응답 반환
- ErrorResponse 기반 JSON 응답 형식 통일

#### Result
인증 실패와 인가 실패를 구분하고 일관된 JSON 오류
응답을 제공하도록 개선하였습니다.

Postman 및 Security 통합 테스트를 통해 주요 인증 및
인가 동작을 검증하였습니다.

### 4. 기존 데이터 유지 및 스키마 변경 관리

#### Problem
기존 데이터를 유지하면서 데이터베이스 컬럼과
제약 조건을 변경하고 관리할 필요가 있었습니다.

#### Cause
Hibernate의 자동 스키마 변경 방식만으로는
데이터베이스 변경 이력과 버전을 체계적으로
관리하기 어렵습니다.

#### Solution
- Flyway 도입
- V1 초기 데이터베이스 스키마 구성
- V2 사용자 생성 시각 컬럼 추가
- V3 이메일 UNIQUE 제약 조건 이름 변경
- Hibernate ddl-auto=validate 설정
- 별도의 테스트 데이터베이스 구성

#### Result
기존 데이터를 유지하면서 데이터베이스 스키마
변경 이력을 버전별로 관리할 수 있도록 개선하였습니다.

테스트 데이터베이스에서 마이그레이션 및 사용자
생성 시각 저장 동작을 검증하였습니다.

### 5. MockMvc 테스트의 인증 사용자 전달 문제

#### Problem
Controller 테스트에서 @AuthenticationPrincipal을
통해 로그인 사용자 정보를 정상적으로 전달받지
못하는 문제가 발생하였습니다.

#### Cause
해당 MockMvc 테스트 환경에 인증 사용자 정보를
Controller 메서드의 파라미터로 전달하기 위한
ArgumentResolver 설정이 누락되어 있었습니다.

#### Solution
- 테스트용 WebMvcConfigurer 구성
- AuthenticationPrincipalArgumentResolver 등록
- SecurityContextHolder에 테스트 인증 정보 설정
- 테스트 종료 후 SecurityContext 정리
- Mockito를 활용한 Service 호출 인자 검증

#### Result
Controller에서 로그인 사용자 ID를 Service 계층으로
정상적으로 전달하도록 테스트 환경을 개선하였습니다.

사용자 및 주문 소유권 검증과 관련된 Controller
테스트가 정상적으로 통과하였습니다.

## 프로젝트의 한계 및 개선 방향

SpringStudy는 백엔드 핵심 기술을 학습하기 위한
REST API 프로젝트입니다.

현재 구현 범위를 바탕으로 다음과 같은 개선 방향을
고려하고 있습니다.

- 전체 주문 조회 및 단건 조회의 접근 권한 강화
- 주문 생성 시 로그인 사용자와 주문 소유자 검증
- 학습용 사용자 생성 API의 접근 정책 개선
- 동시 요청 상황에 대한 데이터 무결성 테스트 추가
- 테스트 커버리지 측정 및 검증 범위 확대

실제 운영 환경에 적용하기 위해서는 추가적인
보안 검토 및 테스트가 필요합니다.