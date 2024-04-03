# 편의점 예약 시스템


## Description
편의점 예약 시스템은 편의점의 인기 상품 재고를 검색하고 예약할 수 있는 웹 기반 시스템을 구축하는 프로젝트입니다. 
<br>사용자는 온라인으로 상품을 검색하고 예약함으로써 매장 방문 전에 필요한 상품을 확보할 수 있습니다.

<hr/>

## Features

- **사용자 인증 및 관리**: 로그인, 회원가입 기능 및 사용자 인증을 제공합니다.
- **상품 검색 및 예약**: 편의점 상품에 대한 검색 기능과 예약 기능을 제공합니다.
- **마이페이지**: 예약 상황을 조회하고 취소, 추가 주문 기능을 제공합니다.

<hr/>

## System Requirements

- **Programming Language**: Java 8
- **Build Tool**: Gradle
- **Web Framework**: Spring Boot 2.7.16
- **Database**: MySQL 8.0
- **Front-end**: HTML5, CSS3, JavaScript

<hr/>

## Getting Started

프로젝트 시작을 위한 기본 단계:

1. **프로젝트 클론하기**
   ```bash
   git clone https://example.com/convenience-store.git
   ```
   
2. **프로젝트 디렉토리로 이동**
   ```bash
   cd convenience-store
   ```

3. **환경 설정 파일 구성**
   `application.properties` 또는 `application.yml` 파일에서 환경 설정.

4. **Spring Boot 애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
   Windows 사용자는 CMD 또는 PowerShell을 사용합니다.

<hr/>

## Source Architecture
```
com.anyang.convenience
├── controller
│   ├── LoginController.java
│   ├── ProductController.java
│   └── ReservationController.java
├── model
│   └── entity
│       ├── Customer.java
│       ├── Product.java
│       ├── Reservation.java
│       └── Store.java
├── repository
│   ├── CustomerRepository.java
│   ├── ProductRepository.java
│   └── ReservationRepository.java
├── service
│  ├── CustomerService.java
│  ├── ProductService.java
│  ├── ReservationService.java
│  └── ReservationService.java
└── ConvenienceApplication.java
```

<hr/>

## Contact
- 프로젝트 관련 문의는 [이메일](lnewgatel@gmail.com)로 연락 주세요.
