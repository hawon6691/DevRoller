# 🎲 DevRoller

개발 프로젝트 주제를 추첨해주는 게이미피케이션 API 서버

> "뭘 만들까 고민하는 시간을 아끼자!"

## ✨ 주요 기능

### 🎰 추첨 시스템
- **랜덤** - 완전 무작위 추첨
- **룰렛** - 덜 뽑힌 주제가 유리한 가중치 룰렛
- **사다리** - 사다리 타기 시뮬레이션
- **제비뽑기** - 난이도별 가중치 제비뽑기

### 🎮 게이미피케이션
- **레벨 & 경험치** - 프로젝트 완료 시 경험치 획득
- **업적** - 다양한 조건의 업적 시스템 (30개+)
- **칭호** - 레벨/업적 기반 칭호 수집
- **스트릭** - 연속 활동 기록
- **상태창** - 웹소설 스타일 사용자 상태창

### 📋 프로젝트 관리
- 추첨된 주제로 프로젝트 시작/완료/포기
- 진행률 추적
- GitHub URL 연동
- 리뷰 & 평점 시스템

### 💡 커뮤니티
- 주제 제안 & 투표
- 북마크
- 랭킹 시스템

## 🛠 기술 스택

- **Backend**: Spring Boot 4.0.1, Java 21
- **Build**: Gradle 8.12
- **Database**: MySQL 8.0
- **Security**: Spring Security OAuth2 Resource Server + JWT (RSA)
- **Documentation**: Swagger/OpenAPI 3.0
- **Migration**: Flyway

## 🚀 시작하기

### 요구사항
- Java 21+
- Docker & Docker Compose
- Gradle 8.12+ (또는 Gradle Wrapper 사용)

### 1. 프로젝트 클론
```bash
git clone https://github.com/your-repo/dev-roller.git
cd dev-roller
```

### 2. Docker MySQL 실행
```bash
docker-compose up -d
```

### 3. 애플리케이션 실행

#### Local 프로필 (H2 인메모리 DB)
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### Dev 프로필 (Docker MySQL)
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 4. API 문서 확인
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console (local): http://localhost:8080/h2-console

## 📁 프로젝트 구조

```
src/main/java/com/devroller/
├── domain/
│   ├── user/           # 사용자, 인증
│   ├── category/       # 카테고리
│   ├── tag/            # 태그
│   ├── idea/           # 아이디어, 사용자 프로젝트
│   ├── pick/           # 추첨
│   ├── gamification/   # 업적, 칭호, 스트릭, 상태창
│   ├── bookmark/       # 북마크
│   ├── review/         # 리뷰
│   └── suggestion/     # 제안
└── global/
    ├── common/         # 공통 클래스
    ├── config/         # 설정
    ├── security/       # 보안
    └── exception/      # 예외 처리
```

## 🔑 API 엔드포인트

### 인증
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| POST | `/api/auth/refresh` | 토큰 갱신 |

### 추첨
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/pick` | 추첨하기 |
| POST | `/api/pick/{ideaId}/start` | 프로젝트 시작 |
| GET | `/api/pick/history` | 추첨 기록 |

### 프로젝트
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/my-projects/in-progress` | 진행중 프로젝트 |
| POST | `/api/my-projects/{ideaId}/complete` | 프로젝트 완료 |
| GET | `/api/my-projects/stats` | 프로젝트 통계 |

### 상태창
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/status/me` | 내 상태창 |
| GET | `/api/status/{userId}` | 사용자 상태창 |

## 📊 데이터베이스 스키마

15개의 테이블로 구성:
- `users` - 사용자
- `categories` - 카테고리
- `tags` - 태그
- `ideas` - 아이디어
- `idea_tags` - 아이디어-태그 연결
- `user_ideas` - 사용자 프로젝트
- `pick_histories` - 추첨 기록
- `achievements` - 업적
- `user_achievements` - 사용자 업적
- `titles` - 칭호
- `user_titles` - 사용자 칭호
- `streaks` - 스트릭
- `bookmarks` - 북마크
- `reviews` - 리뷰
- `suggestions` - 제안

## 🎯 초기 데이터

- **카테고리**: 웹, 모바일, CLI, 게임, AI/ML, 데이터, 자동화, API/백엔드, 데스크톱
- **태그**: Spring Boot, React, Python 등 25개
- **업적**: 30개 (완료 횟수, 스트릭, 카테고리, 난이도, 특별)
- **칭호**: 25개 (레벨 기반, 업적 기반)
- **아이디어**: 35개 샘플 프로젝트

## ⚙️ 환경 설정

### JWT RSA 키 생성 (이미 포함됨)
```bash
# Private Key
openssl genrsa -out src/main/resources/keys/private.pem 2048

# Public Key
openssl rsa -in src/main/resources/keys/private.pem -pubout -out src/main/resources/keys/public.pem
```

### 환경 변수 (prod)
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=devrollerdb
DB_USERNAME=hawon
DB_PASSWORD=your_password
JWT_PUBLIC_KEY_LOCATION=classpath:keys/public.pem
```

## 📝 라이선스

MIT License

---

Made with ❤️ by Hawon