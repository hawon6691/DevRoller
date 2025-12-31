# DevRoller 🎲

> 게이미피케이션 기반 개발 프로젝트 주제 추첨 API

웹소설/웹툰의 **스테이터스 창** 컨셉을 적용한 개발 프로젝트 주제 추천 시스템입니다.

## ✨ 주요 기능

### 🎰 추첨 시스템
- **랜덤**: 완전 무작위 추첨
- **룰렛**: 인기도 역가중치 적용 (덜 선택된 주제가 더 높은 확률)
- **사다리**: 후보 중 무작위 선택
- **복권**: 난이도별 가중치 적용 (어려운 주제가 더 높은 확률)

### 📊 게이미피케이션
- **레벨 시스템**: 50레벨까지 성장
- **경험치**: 프로젝트 완료 시 난이도별 차등 지급 (Easy: 50, Medium: 100, Hard: 200)
- **업적**: 13가지 업적 (완료 횟수, 스트릭, 난이도, 특별 업적)
- **칭호**: 6단계 희귀도 (Common ~ Legendary)
- **스트릭**: 연속 완료일 추적

### 👤 사용자 기능
- JWT 기반 인증 (RSA)
- 프로필 관리
- 랭킹 시스템 (레벨/완료/스트릭)
- 북마크 & 리뷰

## 🛠 기술 스택

- **Backend**: Spring Boot 3.4.1, Java 21
- **Database**: MySQL 8.0, Flyway Migration
- **Security**: Spring Security, OAuth2 Resource Server (JWT)
- **Documentation**: Swagger/OpenAPI 3.0
- **Build**: Gradle 8.11

## 📁 프로젝트 구조

```
src/main/java/com/devroller/
├── domain/
│   ├── auth/           # 인증 (회원가입, 로그인, 토큰)
│   ├── user/           # 사용자 관리
│   ├── idea/           # 아이디어/프로젝트
│   ├── pick/           # 추첨 시스템
│   ├── gamification/   # 게이미피케이션
│   │   ├── achievement/  # 업적
│   │   ├── title/        # 칭호
│   │   ├── streak/       # 스트릭
│   │   └── event/        # 이벤트 리스너
│   ├── category/       # 카테고리
│   ├── tag/            # 태그
│   ├── bookmark/       # 북마크
│   ├── review/         # 리뷰
│   └── suggestion/     # 주제 제안
└── global/
    ├── common/         # 공통 클래스
    ├── config/         # 설정
    ├── exception/      # 예외 처리
    └── security/       # 보안 설정
```

## 🚀 시작하기

### 요구사항
- Java 21+
- MySQL 8.0+
- Gradle 8.x

### 설정

1. **데이터베이스 생성**
```sql
CREATE DATABASE devrollerdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'hawon'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON devrollerdb.* TO 'hawon'@'localhost';
```

2. **환경별 실행**
```bash
# 개발 환경 (MySQL)
./gradlew bootRun --args='--spring.profiles.active=dev'

# 로컬 환경 (H2 인메모리)
./gradlew bootRun --args='--spring.profiles.active=local'
```

3. **Swagger UI 접속**
```
http://localhost:8080/swagger-ui.html
```

## 📝 API 엔드포인트

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/signup` | 회원가입 |
| POST | `/api/v1/auth/login` | 로그인 |
| POST | `/api/v1/auth/refresh` | 토큰 갱신 |

### User
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/me` | 내 정보 조회 |
| PATCH | `/api/v1/users/me` | 프로필 수정 |
| GET | `/api/v1/users/ranking/level` | 레벨 랭킹 |

### Pick
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/picks` | 추첨 실행 |
| GET | `/api/v1/picks/history` | 추첨 기록 |
| GET | `/api/v1/picks/stats` | 추첨 통계 |

### Idea
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/ideas` | 아이디어 목록 |
| GET | `/api/v1/ideas/{id}` | 아이디어 상세 |
| POST | `/api/v1/ideas/{id}/start` | 프로젝트 시작 |
| POST | `/api/v1/ideas/{id}/complete` | 프로젝트 완료 |

### Gamification
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/gamification/status` | 스테이터스 창 |
| GET | `/api/v1/gamification/achievements` | 업적 목록 |
| GET | `/api/v1/gamification/titles` | 칭호 목록 |
| POST | `/api/v1/gamification/titles/{id}/equip` | 칭호 장착 |

## 🎮 게이미피케이션 상세

### 업적 목록
| 코드 | 이름 | 조건 | 보상 XP |
|------|------|------|---------|
| FIRST_COMPLETE | 첫 번째 완료 | 프로젝트 1개 완료 | 100 |
| COMPLETE_5 | 성장하는 개발자 | 프로젝트 5개 완료 | 200 |
| STREAK_7 | 일주일 연속 | 7일 연속 완료 | 300 |
| HARD_FIRST | 도전자 | HARD 프로젝트 첫 완료 | 200 |

### 칭호 희귀도
- 🟢 **Common**: 기본 칭호
- 🔵 **Uncommon**: 레벨 10+ 달성
- 🟣 **Rare**: 레벨 20+ 또는 특정 업적
- 🟠 **Epic**: 레벨 30+ 또는 어려운 업적
- 🟡 **Legendary**: 레벨 40+ 또는 최고 업적

## 🔧 개발 도구

### 테스트 실행
```bash
./gradlew test
```

### 빌드
```bash
./gradlew build
```

### Docker (MySQL)
```bash
docker-compose up -d
```

## 📄 라이선스

MIT License

---

Made with ❤️ for developers who can't decide what to build next
