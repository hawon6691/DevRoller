**프로젝트 기간:** 2025-12-29 ~ 2026-01-02

---

## 1\. 프로젝트 개요

### 1.1 프로젝트 목적

개인 프로젝트를 진행할 때마다 "무엇을 만들지" 고민하는 시간을 줄이고, 다양한 추첨 방식으로 재미있게 개발 주제를 선택할 수 있는 API 서버를 구축한다. **상태창 시스템**을 도입하여 게이미피케이션 요소를 메인으로 한 개발자 성장 트래킹 서비스를 제공한다.

### 1.2 주요 기능

1.  **다양한 추첨 시스템** - 룰렛, 사다리, 제비뽑기, 랜덤 등 다양한 방식으로 주제 추첨
2.  **상태창 시스템** - 레벨, 경험치, 업적, 스트릭 등 게이미피케이션 요소
3.  **프로젝트 관리** - 주제별 진행 상태(대기/진행중/완료) 관리
4.  **통계 및 랭킹** - 개인 통계, 인기 주제, 사용자 랭킹
5.  **소셜 기능** - 완성작 공유, 주제 제안, 좋아요

### 1.3 기술 스택

-   **Backend:** Java 21, Spring Boot 4.x, Spring Data JPA
-   **Database:** MySQL 8.x
-   **Build Tool:** Gradle
-   **보안:** Spring Security, JWT, BCrypt
-   **문서화:** Swagger (SpringDoc OpenAPI)
-   **배포:** Docker

---

## 2\. 요구사항 정의

### 2.1 기능 요구사항

#### 추첨 시스템

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 1 | 랜덤 추첨 | 높음 | 전체 주제 중 무작위 1개 또는 N개 추천 |
| 2 | 룰렛 추첨 | 높음 | 룰렛 방식으로 주제 선택 (애니메이션용 데이터 제공) |
| 3 | 사다리 추첨 | 중간 | 사다리 타기 방식 데이터 제공 |
| 4 | 제비뽑기 | 중간 | 제비뽑기 방식 데이터 제공 |
| 5 | 필터링 추첨 | 높음 | 카테고리/난이도/태그 조건으로 필터링 후 추첨 |
| 6 | 제외 추첨 | 높음 | 진행중/완료 주제 제외하고 추첨 |

#### 주제 관리

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 7 | 주제 CRUD | 높음 | 주제 생성/조회/수정/삭제 |
| 8 | 카테고리 관리 | 높음 | 웹, 모바일, CLI, 게임 등 카테고리 |
| 9 | 태그 시스템 | 중간 | #REST, #실시간, #CRUD 등 태그 |
| 10 | 기술 스택 추천 | 중간 | 주제별 추천 기술 스택 |
| 11 | 예상 소요 시간 | 중간 | 난이도별 예상 개발 기간 |
| 12 | 참고 자료 | 낮음 | 관련 튜토리얼, GitHub 링크 |

#### 사용자 기능

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 13 | 회원가입/로그인 | 높음 | JWT 기반 인증 |
| 14 | 상태 관리 | 높음 | 주제별 대기/진행중/완료 상태 |
| 15 | 북마크 | 중간 | 관심 주제 저장 |
| 16 | 평점/리뷰 | 중간 | 완료한 주제에 별점, 후기 작성 |
| 17 | 추천 이력 | 중간 | 추첨받은 주제 기록 |
| 18 | 주제 숨기기 | 낮음 | 관심 없는 주제 제외 |

#### 게이미피케이션 (상태창 시스템)

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 19 | 레벨 시스템 | 높음 | 경험치 기반 레벨업 |
| 20 | 경험치(XP) | 높음 | 난이도별 차등 경험치 |
| 21 | 업적/뱃지 | 높음 | 조건 달성 시 업적 해금 |
| 22 | 스트릭 | 중간 | 연속 완료 기록 |
| 23 | 칭호 시스템 | 중간 | 레벨/업적에 따른 칭호 부여 |
| 24 | 상태창 조회 | 높음 | 전체 스탯을 상태창 형태로 조회 |

#### 통계/랭킹

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 25 | 개인 통계 | 중간 | 완료 수, 카테고리별 분포 등 |
| 26 | 인기 주제 | 중간 | 가장 많이 추천/완료된 주제 TOP N |
| 27 | 사용자 랭킹 | 중간 | 레벨, 완료 수 기준 랭킹 |
| 28 | 시즌 랭킹 | 낮음 | 분기별 랭킹 초기화 |

#### 소셜/커뮤니티

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 29 | 완성작 공유 | 중간 | GitHub 링크로 결과물 공유 |
| 30 | 주제 제안 | 낮음 | 사용자가 새 주제 제안 |
| 31 | 좋아요 | 낮음 | 주제/완성작에 좋아요 |

#### 프로젝트 관리

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 32 | 진행률 트래킹 | 중간 | 0~100% 진행도 |
| 33 | 데드라인 설정 | 낮음 | 목표 완료일 지정 |
| 34 | GitHub 연동 | 낮음 | 레포 생성/완료 감지 |

#### 시즌/이벤트

| 번호 | 기능 | 우선순위 | 설명 |
| --- | --- | --- | --- |
| 35 | 월간 챌린지 | 낮음 | 이번 달 테마 주제 |
| 36 | 특별 이벤트 | 낮음 | 해커톤 스타일 챌린지 |

### 2.2 비기능 요구사항

-   **성능:** API 응답 시간 200ms 이내
-   **보안:** JWT 토큰 인증, 비밀번호 BCrypt 암호화
-   **확장성:** 마이크로서비스 분리 가능한 구조 설계

---

## 3\. DB 설계

### 3.1 ERD

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   users     │       │   ideas     │       │ categories  │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)     │       │ id (PK)     │
│ email       │       │ category_id │───────│ name        │
│ password    │       │ title       │       │ description │
│ nickname    │       │ description │       │ icon        │
│ level       │       │ difficulty  │       └─────────────┘
│ xp          │       │ est_hours   │
│ title_id    │       │ ref_url     │       ┌─────────────┐
│ streak      │       │ pick_count  │       │    tags     │
│ created_at  │       │ created_at  │       ├─────────────┤
└─────────────┘       └─────────────┘       │ id (PK)     │
      │                     │               │ name        │
      │                     │               └─────────────┘
      │                     │                     │
      ▼                     ▼                     ▼
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│ user_ideas  │       │ idea_tags   │       │ idea_techs  │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │       │ idea_id(FK) │       │ idea_id(FK) │
│ user_id(FK) │       │ tag_id (FK) │       │ tech_id(FK) │
│ idea_id(FK) │       └─────────────┘       └─────────────┘
│ status      │                                   │
│ progress    │       ┌─────────────┐             ▼
│ github_url  │       │ tech_stacks │       ┌─────────────┐
│ deadline    │       ├─────────────┤       │   titles    │
│ started_at  │       │ id (PK)     │       ├─────────────┤
│ completed_at│       │ name        │       │ id (PK)     │
└─────────────┘       │ icon        │       │ name        │
      │               └─────────────┘       │ min_level   │
      │                                     │ description │
      ▼                                     └─────────────┘
┌─────────────┐       ┌─────────────┐       
│  bookmarks  │       │   reviews   │       ┌─────────────┐
├─────────────┤       ├─────────────┤       │achievements │
│ id (PK)     │       │ id (PK)     │       ├─────────────┤
│ user_id(FK) │       │ user_id(FK) │       │ id (PK)     │
│ idea_id(FK) │       │ idea_id(FK) │       │ name        │
│ created_at  │       │ rating      │       │ description │
└─────────────┘       │ content     │       │ condition   │
                      │ created_at  │       │ xp_reward   │
┌─────────────┐       └─────────────┘       │ badge_icon  │
│pick_history │                             └─────────────┘
├─────────────┤       ┌─────────────┐             │
│ id (PK)     │       │ idea_likes  │             ▼
│ user_id(FK) │       ├─────────────┤       ┌─────────────┐
│ idea_id(FK) │       │ id (PK)     │       │user_achieve │
│ pick_type   │       │ user_id(FK) │       ├─────────────┤
│ picked_at   │       │ idea_id(FK) │       │ id (PK)     │
└─────────────┘       │ created_at  │       │ user_id(FK) │
                      └─────────────┘       │ achieve_id  │
┌─────────────┐                             │ achieved_at │
│hidden_ideas │                             └─────────────┘
├─────────────┤       
│ id (PK)     │       ┌─────────────┐
│ user_id(FK) │       │idea_suggest │
│ idea_id(FK) │       ├─────────────┤
│ created_at  │       │ id (PK)     │
└─────────────┘       │ user_id(FK) │
                      │ title       │
                      │ description │
                      │ status      │
                      │ created_at  │
                      └─────────────┘
```

### 3.2 테이블 명세

#### users 테이블 (사용자)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | 사용자 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 |
| password | VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| nickname | VARCHAR(50) | UNIQUE, NOT NULL | 닉네임 |
| level | INT | DEFAULT 1 | 현재 레벨 |
| xp | INT | DEFAULT 0 | 현재 경험치 |
| total\_xp | INT | DEFAULT 0 | 누적 경험치 |
| title\_id | BIGINT | FK, NULLABLE | 장착 칭호 |
| current\_streak | INT | DEFAULT 0 | 현재 연속 완료 |
| max\_streak | INT | DEFAULT 0 | 최대 연속 완료 |
| last\_completed\_at | DATE | NULLABLE | 마지막 완료일 |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 가입일 |
| updated\_at | DATETIME | ON UPDATE CURRENT\_TIMESTAMP | 수정일 |

#### categories 테이블 (카테고리)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | 카테고리 ID |
| name | VARCHAR(50) | UNIQUE, NOT NULL | 카테고리명 |
| description | VARCHAR(255) | NULLABLE | 설명 |
| icon | VARCHAR(50) | NULLABLE | 아이콘 |

#### ideas 테이블 (개발 주제)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | 주제 ID |
| category\_id | BIGINT | FK, NOT NULL | 카테고리 ID |
| title | VARCHAR(100) | NOT NULL | 주제명 |
| description | TEXT | NULLABLE | 상세 설명 |
| difficulty | ENUM | NOT NULL | 'BEGINNER', 'INTERMEDIATE', 'ADVANCED' |
| estimated\_hours | INT | NULLABLE | 예상 소요 시간 |
| reference\_url | VARCHAR(500) | NULLABLE | 참고 자료 URL |
| pick\_count | INT | DEFAULT 0 | 추천된 횟수 |
| complete\_count | INT | DEFAULT 0 | 완료된 횟수 |
| is\_active | BOOLEAN | DEFAULT TRUE | 활성화 여부 |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 생성일 |

#### tags 테이블 (태그)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | 태그 ID |
| name | VARCHAR(30) | UNIQUE, NOT NULL | 태그명 |

#### idea\_tags 테이블 (주제-태그 연결)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| tag\_id | BIGINT | FK, NOT NULL | 태그 ID |
| PRIMARY KEY | (idea\_id, tag\_id) |   | 복합키 |

#### tech\_stacks 테이블 (기술 스택)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | 기술 스택 ID |
| name | VARCHAR(50) | UNIQUE, NOT NULL | 기술명 |
| icon | VARCHAR(50) | NULLABLE | 아이콘 |

#### idea\_tech\_stacks 테이블 (주제-기술스택 연결)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| tech\_stack\_id | BIGINT | FK, NOT NULL | 기술 스택 ID |
| PRIMARY KEY | (idea\_id, tech\_stack\_id) |   | 복합키 |

#### user\_ideas 테이블 (사용자별 주제 상태)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| status | ENUM | NOT NULL | 'PENDING', 'IN\_PROGRESS', 'COMPLETED' |
| progress | INT | DEFAULT 0 | 진행률 (0-100) |
| github\_url | VARCHAR(500) | NULLABLE | GitHub 레포 URL |
| deadline | DATE | NULLABLE | 목표 완료일 |
| started\_at | DATETIME | NULLABLE | 시작일 |
| completed\_at | DATETIME | NULLABLE | 완료일 |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 생성일 |
| UNIQUE | (user\_id, idea\_id) |   | 유니크 제약 |

#### bookmarks 테이블 (북마크)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 생성일 |
| UNIQUE | (user\_id, idea\_id) |   | 유니크 제약 |

#### reviews 테이블 (평점/리뷰)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| rating | INT | NOT NULL, CHECK(1-5) | 별점 (1-5) |
| content | TEXT | NULLABLE | 리뷰 내용 |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 생성일 |
| UNIQUE | (user\_id, idea\_id) |   | 유니크 제약 |

#### pick\_history 테이블 (추천 이력)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| pick\_type | ENUM | NOT NULL | 'RANDOM', 'ROULETTE', 'LADDER', 'DRAW' |
| picked\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 추첨일시 |

#### titles 테이블 (칭호)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| name | VARCHAR(50) | UNIQUE, NOT NULL | 칭호명 |
| description | VARCHAR(255) | NULLABLE | 설명 |
| min\_level | INT | DEFAULT 1 | 필요 최소 레벨 |
| condition\_type | VARCHAR(50) | NULLABLE | 획득 조건 타입 |
| condition\_value | INT | NULLABLE | 획득 조건 값 |

#### achievements 테이블 (업적)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| name | VARCHAR(100) | UNIQUE, NOT NULL | 업적명 |
| description | VARCHAR(255) | NOT NULL | 업적 설명 |
| condition\_type | VARCHAR(50) | NOT NULL | 조건 타입 |
| condition\_value | INT | NOT NULL | 조건 값 |
| xp\_reward | INT | DEFAULT 0 | 보상 경험치 |
| badge\_icon | VARCHAR(50) | NULLABLE | 뱃지 아이콘 |

#### user\_achievements 테이블 (사용자 업적)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| achievement\_id | BIGINT | FK, NOT NULL | 업적 ID |
| achieved\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 달성일시 |
| UNIQUE | (user\_id, achievement\_id) |   | 유니크 제약 |

#### idea\_likes 테이블 (좋아요)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 생성일 |
| UNIQUE | (user\_id, idea\_id) |   | 유니크 제약 |

#### hidden\_ideas 테이블 (숨긴 주제)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 사용자 ID |
| idea\_id | BIGINT | FK, NOT NULL | 주제 ID |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 생성일 |
| UNIQUE | (user\_id, idea\_id) |   | 유니크 제약 |

#### idea\_suggestions 테이블 (주제 제안)

| 컬럼명 | 타입 | 제약조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT | PRIMARY KEY, AUTO\_INCREMENT | ID |
| user\_id | BIGINT | FK, NOT NULL | 제안자 ID |
| title | VARCHAR(100) | NOT NULL | 제안 주제명 |
| description | TEXT | NULLABLE | 설명 |
| category\_id | BIGINT | FK, NULLABLE | 카테고리 ID |
| status | ENUM | DEFAULT 'PENDING' | 'PENDING', 'APPROVED', 'REJECTED' |
| created\_at | DATETIME | DEFAULT CURRENT\_TIMESTAMP | 제안일 |

### 3.3 인덱스 설계

```
-- 사용자 조회
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_nickname ON users(nickname);
CREATE INDEX idx_users_level ON users(level DESC);

-- 주제 조회
CREATE INDEX idx_ideas_category ON ideas(category_id);
CREATE INDEX idx_ideas_difficulty ON ideas(difficulty);
CREATE INDEX idx_ideas_pick_count ON ideas(pick_count DESC);

-- 사용자 주제 상태
CREATE INDEX idx_user_ideas_user ON user_ideas(user_id);
CREATE INDEX idx_user_ideas_status ON user_ideas(user_id, status);

-- 추천 이력
CREATE INDEX idx_pick_history_user ON pick_history(user_id, picked_at DESC);

-- 북마크
CREATE INDEX idx_bookmarks_user ON bookmarks(user_id);

-- 리뷰
CREATE INDEX idx_reviews_idea ON reviews(idea_id);
```

---

## 4\. API 명세서

### 4.1 회원 관리

#### POST /api/auth/register

**회원가입**

**요청**

```
{
  "email": "developer@example.com",
  "password": "password123!",
  "nickname": "개발자김"
}
```

**응답**

```
{
  "success": true,
  "message": "회원가입 성공",
  "data": {
    "id": 1,
    "email": "developer@example.com",
    "nickname": "개발자김",
    "level": 1,
    "xp": 0
  }
}
```

#### POST /api/auth/login

**로그인**

**요청**

```
{
  "email": "developer@example.com",
  "password": "password123!"
}
```

**응답**

```
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
      "id": 1,
      "email": "developer@example.com",
      "nickname": "개발자김",
      "level": 5,
      "title": "코딩 입문자"
    }
  }
}
```

---

### 4.2 추첨 시스템

#### GET /api/pick/random

**랜덤 추첨**

**Query Parameters**  
| 파라미터 | 타입 | 필수 | 설명 |  
|----------|------|------|------|  
| count | int | X | 추첨 개수 (기본값: 1, 최대: 10) |  
| category | string | X | 카테고리 필터 |  
| difficulty | string | X | 난이도 필터 (BEGINNER/INTERMEDIATE/ADVANCED) |  
| tags | string | X | 태그 필터 (쉼표 구분) |  
| excludeCompleted | boolean | X | 완료 주제 제외 (기본값: false) |  
| excludeInProgress | boolean | X | 진행중 주제 제외 (기본값: false) |

**응답**

```
{
  "success": true,
  "data": {
    "pickType": "RANDOM",
    "ideas": [
      {
        "id": 15,
        "title": "실시간 채팅 애플리케이션",
        "description": "WebSocket을 이용한 실시간 채팅 앱",
        "category": {
          "id": 1,
          "name": "웹",
          "icon": "🌐"
        },
        "difficulty": "INTERMEDIATE",
        "estimatedHours": 40,
        "techStacks": ["Spring Boot", "WebSocket", "Redis"],
        "tags": ["실시간", "채팅", "WebSocket"]
      }
    ]
  }
}
```

#### GET /api/pick/roulette

**룰렛 추첨**

**Query Parameters**  
동일 (count 제외 - 룰렛은 항상 1개)

**응답**

```
{
  "success": true,
  "data": {
    "pickType": "ROULETTE",
    "candidates": [
      {"id": 1, "title": "TODO 앱", "color": "#FF6B6B"},
      {"id": 2, "title": "블로그 API", "color": "#4ECDC4"},
      {"id": 3, "title": "채팅 앱", "color": "#45B7D1"},
      {"id": 4, "title": "날씨 앱", "color": "#96CEB4"},
      {"id": 5, "title": "가계부", "color": "#FFEAA7"}
    ],
    "selectedIndex": 2,
    "selectedIdea": {
      "id": 3,
      "title": "채팅 앱",
      "description": "...",
      "difficulty": "INTERMEDIATE"
    }
  }
}
```

#### GET /api/pick/ladder

**사다리 추첨**

**응답**

```
{
  "success": true,
  "data": {
    "pickType": "LADDER",
    "ladderData": {
      "participants": ["A", "B", "C", "D"],
      "ideas": [
        {"id": 1, "title": "TODO 앱"},
        {"id": 2, "title": "블로그 API"},
        {"id": 3, "title": "채팅 앱"},
        {"id": 4, "title": "날씨 앱"}
      ],
      "connections": [[0,1,3], [1,2,5], [2,0,7], [3,3,2]],
      "result": {
        "selectedPath": 0,
        "selectedIdea": {"id": 3, "title": "채팅 앱"}
      }
    }
  }
}
```

#### GET /api/pick/draw

**제비뽑기 추첨**

**응답**

```
{
  "success": true,
  "data": {
    "pickType": "DRAW",
    "totalSticks": 5,
    "winningIndex": 2,
    "selectedIdea": {
      "id": 7,
      "title": "URL 단축 서비스",
      "description": "...",
      "difficulty": "BEGINNER"
    }
  }
}
```

---

### 4.3 주제 관리

#### GET /api/ideas

**주제 목록 조회**

**Query Parameters**  
| 파라미터 | 타입 | 필수 | 설명 |  
|----------|------|------|------|  
| page | int | X | 페이지 번호 (기본값: 0) |  
| size | int | X | 페이지 크기 (기본값: 20) |  
| category | string | X | 카테고리 필터 |  
| difficulty | string | X | 난이도 필터 |  
| tags | string | X | 태그 필터 |  
| sort | string | X | 정렬 기준 (pickCount, createdAt) |

**응답**

```
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "TODO 리스트 API",
        "category": {"id": 1, "name": "웹"},
        "difficulty": "BEGINNER",
        "estimatedHours": 8,
        "pickCount": 150,
        "completeCount": 45,
        "tags": ["CRUD", "REST"]
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### GET /api/ideas/{id}

**주제 상세 조회**

**응답**

```
{
  "success": true,
  "data": {
    "id": 1,
    "title": "TODO 리스트 API",
    "description": "CRUD 기능을 갖춘 TODO 리스트 REST API 서버",
    "category": {"id": 1, "name": "웹", "icon": "🌐"},
    "difficulty": "BEGINNER",
    "estimatedHours": 8,
    "referenceUrl": "https://github.com/example/todo-api",
    "techStacks": [
      {"id": 1, "name": "Spring Boot", "icon": "🍃"},
      {"id": 2, "name": "MySQL", "icon": "🐬"},
      {"id": 3, "name": "JPA", "icon": "📦"}
    ],
    "tags": ["CRUD", "REST", "입문"],
    "pickCount": 150,
    "completeCount": 45,
    "averageRating": 4.2,
    "reviewCount": 23
  }
}
```

#### POST /api/ideas (관리자)

**주제 등록**

**요청**

```
{
  "title": "실시간 주식 시세 대시보드",
  "description": "WebSocket을 활용한 실시간 주식 데이터 시각화",
  "categoryId": 1,
  "difficulty": "ADVANCED",
  "estimatedHours": 80,
  "referenceUrl": "https://example.com/tutorial",
  "techStackIds": [1, 5, 8],
  "tags": ["실시간", "WebSocket", "차트", "금융"]
}
```

---

### 4.4 사용자 주제 관리

#### POST /api/user/ideas/{ideaId}/start

**주제 시작하기**

**응답**

```
{
  "success": true,
  "message": "프로젝트를 시작합니다!",
  "data": {
    "id": 1,
    "ideaId": 15,
    "status": "IN_PROGRESS",
    "progress": 0,
    "startedAt": "2025-01-15T10:30:00"
  }
}
```

#### PATCH /api/user/ideas/{ideaId}/progress

**진행률 업데이트**

**요청**

```
{
  "progress": 50,
  "githubUrl": "https://github.com/user/my-project"
}
```

#### POST /api/user/ideas/{ideaId}/complete

**주제 완료**

**응답**

```
{
  "success": true,
  "message": "축하합니다! 프로젝트를 완료했습니다!",
  "data": {
    "completedIdea": {
      "id": 15,
      "title": "실시간 채팅 애플리케이션"
    },
    "rewards": {
      "xpEarned": 150,
      "newLevel": 6,
      "levelUp": true,
      "streakUpdated": 5,
      "newAchievements": [
        {
          "id": 3,
          "name": "중급 개발자",
          "description": "중급 난이도 프로젝트 첫 완료",
          "badgeIcon": "🥈"
        }
      ],
      "newTitles": [
        {
          "id": 2,
          "name": "꾸준한 개발자"
        }
      ]
    }
  }
}
```

#### GET /api/user/ideas

**내 프로젝트 목록**

**Query Parameters**  
| 파라미터 | 타입 | 필수 | 설명 |  
|----------|------|------|------|  
| status | string | X | 상태 필터 (PENDING/IN\_PROGRESS/COMPLETED) |

**응답**

```
{
  "success": true,
  "data": [
    {
      "id": 1,
      "idea": {
        "id": 15,
        "title": "실시간 채팅 앱",
        "difficulty": "INTERMEDIATE"
      },
      "status": "IN_PROGRESS",
      "progress": 65,
      "githubUrl": "https://github.com/user/chat-app",
      "startedAt": "2025-01-10T09:00:00",
      "deadline": "2025-02-10"
    }
  ]
}
```

---

### 4.5 상태창 시스템 (게이미피케이션)

#### GET /api/user/status

**상태창 조회 (메인)**

**응답**

```
{
  "success": true,
  "data": {
    "profile": {
      "nickname": "개발자김",
      "title": "성장하는 개발자",
      "profileImage": null
    },
    "stats": {
      "level": 12,
      "currentXp": 450,
      "requiredXp": 1000,
      "xpProgress": 45,
      "totalXp": 5450
    },
    "streak": {
      "current": 7,
      "max": 15,
      "lastCompletedAt": "2025-01-14"
    },
    "projectStats": {
      "total": 23,
      "completed": 18,
      "inProgress": 3,
      "pending": 2
    },
    "categoryStats": [
      {"category": "웹", "count": 10},
      {"category": "모바일", "count": 5},
      {"category": "CLI", "count": 3}
    ],
    "recentAchievements": [
      {
        "id": 5,
        "name": "연속 7일 달성",
        "badgeIcon": "🔥",
        "achievedAt": "2025-01-14"
      }
    ],
    "rank": {
      "total": 156,
      "percentile": 12
    }
  }
}
```

#### GET /api/user/achievements

**업적 목록**

**응답**

```
{
  "success": true,
  "data": {
    "achieved": [
      {
        "id": 1,
        "name": "첫 발걸음",
        "description": "첫 번째 프로젝트 완료",
        "badgeIcon": "🎉",
        "xpReward": 50,
        "achievedAt": "2025-01-05T14:30:00"
      }
    ],
    "notAchieved": [
      {
        "id": 10,
        "name": "풀스택 마스터",
        "description": "모든 카테고리에서 프로젝트 완료",
        "badgeIcon": "👑",
        "xpReward": 500,
        "progress": {
          "current": 4,
          "required": 6
        }
      }
    ],
    "summary": {
      "totalAchievements": 25,
      "achieved": 12,
      "progress": 48
    }
  }
}
```

#### GET /api/user/titles

**칭호 목록**

**응답**

```
{
  "success": true,
  "data": {
    "currentTitle": {
      "id": 5,
      "name": "성장하는 개발자"
    },
    "availableTitles": [
      {"id": 1, "name": "코딩 입문자", "minLevel": 1},
      {"id": 2, "name": "꾸준한 개발자", "minLevel": 5},
      {"id": 5, "name": "성장하는 개발자", "minLevel": 10}
    ],
    "lockedTitles": [
      {"id": 8, "name": "전설의 개발자", "minLevel": 50, "currentLevel": 12}
    ]
  }
}
```

#### PATCH /api/user/titles/{titleId}/equip

**칭호 장착**

---

### 4.6 북마크

#### POST /api/bookmarks/{ideaId}

**북마크 추가**

#### DELETE /api/bookmarks/{ideaId}

**북마크 삭제**

#### GET /api/bookmarks

**북마크 목록**

---

### 4.7 리뷰

#### POST /api/ideas/{ideaId}/reviews

**리뷰 작성**

**요청**

```
{
  "rating": 4,
  "content": "입문자가 하기에 딱 좋은 난이도였습니다. 다만 예상 시간보다 조금 더 걸렸어요."
}
```

#### GET /api/ideas/{ideaId}/reviews

**리뷰 목록**

---

### 4.8 통계/랭킹

#### GET /api/stats/popular

**인기 주제 TOP N**

**응답**

```
{
  "success": true,
  "data": {
    "byPickCount": [
      {"rank": 1, "id": 1, "title": "TODO API", "pickCount": 523},
      {"rank": 2, "id": 5, "title": "채팅 앱", "pickCount": 412}
    ],
    "byCompleteCount": [
      {"rank": 1, "id": 1, "title": "TODO API", "completeCount": 234}
    ],
    "byRating": [
      {"rank": 1, "id": 8, "title": "블로그 API", "avgRating": 4.8}
    ]
  }
}
```

#### GET /api/stats/ranking

**사용자 랭킹**

**Query Parameters**  
| 파라미터 | 타입 | 필수 | 설명 |  
|----------|------|------|------|  
| type | string | X | 랭킹 타입 (level, completed, streak) |  
| limit | int | X | 조회 개수 (기본값: 10) |

**응답**

```
{
  "success": true,
  "data": {
    "rankings": [
      {
        "rank": 1,
        "userId": 42,
        "nickname": "코딩왕",
        "title": "전설의 개발자",
        "level": 35,
        "completedCount": 67,
        "currentStreak": 30
      }
    ],
    "myRank": {
      "rank": 156,
      "percentile": 12
    }
  }
}
```

---

### 4.9 주제 제안

#### POST /api/suggestions

**주제 제안**

**요청**

```
{
  "title": "AI 이미지 분류 앱",
  "description": "TensorFlow를 이용한 이미지 분류 웹 애플리케이션",
  "categoryId": 1
}
```

---

## 5\. 파일 구조

```
dev-idea-picker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/devideapicker/
│   │   │       ├── DevIdeaPickerApplication.java
│   │   │       │
│   │   │       ├── domain/
│   │   │       │   ├── user/
│   │   │       │   │   ├── controller/
│   │   │       │   │   │   └── UserController.java
│   │   │       │   │   ├── service/
│   │   │       │   │   │   └── UserService.java
│   │   │       │   │   ├── repository/
│   │   │       │   │   │   └── UserRepository.java
│   │   │       │   │   ├── entity/
│   │   │       │   │   │   └── User.java
│   │   │       │   │   └── dto/
│   │   │       │   │       ├── UserRequest.java
│   │   │       │   │       └── UserResponse.java
│   │   │       │   │
│   │   │       │   ├── idea/
│   │   │       │   │   ├── controller/
│   │   │       │   │   ├── service/
│   │   │       │   │   ├── repository/
│   │   │       │   │   ├── entity/
│   │   │       │   │   └── dto/
│   │   │       │   │
│   │   │       │   ├── pick/
│   │   │       │   │   ├── controller/
│   │   │       │   │   │   └── PickController.java
│   │   │       │   │   ├── service/
│   │   │       │   │   │   ├── PickService.java
│   │   │       │   │   │   ├── RandomPickStrategy.java
│   │   │       │   │   │   ├── RoulettePickStrategy.java
│   │   │       │   │   │   ├── LadderPickStrategy.java
│   │   │       │   │   │   └── DrawPickStrategy.java
│   │   │       │   │   └── dto/
│   │   │       │   │
│   │   │       │   ├── gamification/
│   │   │       │   │   ├── controller/
│   │   │       │   │   │   └── StatusController.java
│   │   │       │   │   ├── service/
│   │   │       │   │   │   ├── LevelService.java
│   │   │       │   │   │   ├── AchievementService.java
│   │   │       │   │   │   ├── StreakService.java
│   │   │       │   │   │   └── TitleService.java
│   │   │       │   │   ├── entity/
│   │   │       │   │   │   ├── Achievement.java
│   │   │       │   │   │   ├── UserAchievement.java
│   │   │       │   │   │   └── Title.java
│   │   │       │   │   └── dto/
│   │   │       │   │
│   │   │       │   ├── bookmark/
│   │   │       │   ├── review/
│   │   │       │   ├── stats/
│   │   │       │   └── suggestion/
│   │   │       │
│   │   │       ├── global/
│   │   │       │   ├── config/
│   │   │       │   │   ├── SecurityConfig.java
│   │   │       │   │   ├── SwaggerConfig.java
│   │   │       │   │   └── JpaConfig.java
│   │   │       │   ├── security/
│   │   │       │   │   ├── JwtTokenProvider.java
│   │   │       │   │   ├── JwtAuthenticationFilter.java
│   │   │       │   │   └── CustomUserDetailsService.java
│   │   │       │   ├── exception/
│   │   │       │   │   ├── GlobalExceptionHandler.java
│   │   │       │   │   ├── CustomException.java
│   │   │       │   │   └── ErrorCode.java
│   │   │       │   ├── response/
│   │   │       │   │   └── ApiResponse.java
│   │   │       │   └── util/
│   │   │       │
│   │   │       └── infra/
│   │   │           └── init/
│   │   │               └── DataInitializer.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── data/
│   │           ├── categories.json
│   │           ├── ideas.json
│   │           ├── achievements.json
│   │           └── titles.json
│   │
│   └── test/
│       └── java/
│           └── com/devideapicker/
│               ├── domain/
│               │   ├── pick/
│               │   │   └── PickServiceTest.java
│               │   └── gamification/
│               │       └── LevelServiceTest.java
│               └── integration/
│
├── build.gradle
├── settings.gradle
├── docker-compose.yml
├── Dockerfile
└── README.md
```

---

## 6\. 게이미피케이션 상세 설계

### 6.1 레벨 시스템

| 레벨 | 필요 누적 XP | 칭호 해금 |
| --- | --- | --- |
| 1 | 0 | 코딩 입문자 |
| 5 | 500 | 꾸준한 개발자 |
| 10 | 1,500 | 성장하는 개발자 |
| 20 | 5,000 | 숙련된 개발자 |
| 30 | 12,000 | 베테랑 개발자 |
| 50 | 30,000 | 전설의 개발자 |

### 6.2 경험치 획득

| 행동 | 획득 XP |
| --- | --- |
| 프로젝트 완료 (초급) | 50 XP |
| 프로젝트 완료 (중급) | 100 XP |
| 프로젝트 완료 (고급) | 200 XP |
| 리뷰 작성 | 10 XP |
| 업적 달성 | 업적별 상이 |
| 연속 완료 보너스 | streak × 5 XP |

### 6.3 업적 목록

| 업적명 | 조건 | 보상 XP | 뱃지 |
| --- | --- | --- | --- |
| 첫 발걸음 | 첫 프로젝트 완료 | 50 | 🎉 |
| 열정의 시작 | 5개 프로젝트 완료 | 100 | ⭐ |
| 꾸준함의 힘 | 10개 프로젝트 완료 | 200 | 💪 |
| 연속 3일 | 3일 연속 완료 | 30 | 🔥 |
| 연속 7일 | 7일 연속 완료 | 100 | 🔥🔥 |
| 연속 30일 | 30일 연속 완료 | 500 | 🏆 |
| 초급 마스터 | 초급 10개 완료 | 100 | 🥉 |
| 중급 마스터 | 중급 10개 완료 | 200 | 🥈 |
| 고급 마스터 | 고급 5개 완료 | 300 | 🥇 |
| 풀스택 입문 | 3개 카테고리 완료 | 150 | 🌈 |
| 풀스택 마스터 | 모든 카테고리 완료 | 500 | 👑 |
| 첫 리뷰 | 첫 리뷰 작성 | 20 | 📝 |
| 리뷰어 | 10개 리뷰 작성 | 100 | ✍️ |

---

## 7\. 샘플 데이터

### 7.1 카테고리

| ID | 이름 | 아이콘 |
| --- | --- | --- |
| 1 | 웹 | 🌐 |
| 2 | 모바일 | 📱 |
| 3 | CLI | 💻 |
| 4 | 게임 | 🎮 |
| 5 | 데이터/분석 | 📊 |
| 6 | 자동화/봇 | 🤖 |
| 7 | 데스크톱 | 🖥️ |

### 7.2 샘플 주제 (일부)

| 제목 | 카테고리 | 난이도 | 예상 시간 |
| --- | --- | --- | --- |
| TODO 리스트 API | 웹 | 초급 | 8시간 |
| 블로그 REST API | 웹 | 초급 | 16시간 |
| URL 단축 서비스 | 웹 | 초급 | 12시간 |
| 실시간 채팅 앱 | 웹 | 중급 | 40시간 |
| JWT 인증 서버 | 웹 | 중급 | 24시간 |
| 파일 업로드 서비스 | 웹 | 중급 | 20시간 |
| 결제 시스템 연동 | 웹 | 고급 | 60시간 |
| 검색 엔진 | 웹 | 고급 | 80시간 |
| CLI 계산기 | CLI | 초급 | 4시간 |
| Git 커밋 자동화 도구 | CLI | 중급 | 16시간 |
| 디스코드 봇 | 자동화/봇 | 중급 | 24시간 |
| 웹 스크래퍼 | 데이터/분석 | 중급 | 20시간 |
| 틱택토 게임 | 게임 | 초급 | 8시간 |
| 타이핑 게임 | 게임 | 중급 | 24시간 |

---

## 8\. 참고 자료

-   [Spring Boot 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/)
-   [Spring Security JWT](https://spring.io/guides/tutorials/spring-security-and-angular-js/)
-   [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
-   [Swagger/OpenAPI](https://springdoc.org/)