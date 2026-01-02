-- =====================================================
-- DevRoller Sample Ideas Data
-- V3: Sample Ideas for Each Category
-- =====================================================

-- 웹 프론트엔드 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('Todo List 앱', '기본적인 할일 관리 웹 앱. CRUD, 필터링, 로컬 스토리지 저장 기능 구현', 1, 'EASY', 10, 'HTML, CSS, JavaScript', NULL),
('날씨 대시보드', '날씨 API를 활용한 실시간 날씨 정보 대시보드', 1, 'EASY', 15, 'React, Weather API, CSS', NULL),
('포트폴리오 웹사이트', '개인 포트폴리오를 위한 반응형 웹사이트', 1, 'MEDIUM', 20, 'React, Tailwind CSS, Framer Motion', NULL),
('칸반 보드', 'Trello 스타일의 드래그 앤 드롭 칸반 보드', 1, 'MEDIUM', 30, 'React, DnD Kit, TypeScript', NULL),
('실시간 채팅 UI', 'WebSocket을 활용한 실시간 채팅 인터페이스', 1, 'HARD', 40, 'React, Socket.io, Redux', NULL);

-- 웹 백엔드 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('URL 단축기', 'URL을 짧게 변환하고 클릭 통계를 추적하는 서비스', 2, 'EASY', 15, 'Spring Boot, MySQL, Redis', NULL),
('파일 업로드 서버', '파일 업로드/다운로드 및 미리보기 기능이 있는 서버', 2, 'MEDIUM', 25, 'Spring Boot, AWS S3, JPA', NULL),
('OAuth2 인증 서버', 'Google, GitHub 로그인을 지원하는 인증 서버', 2, 'MEDIUM', 35, 'Spring Security, OAuth2, JWT', NULL),
('RESTful API 서버', '블로그 플랫폼을 위한 완전한 REST API', 2, 'MEDIUM', 40, 'Spring Boot, JPA, Swagger', NULL),
('실시간 알림 서버', 'SSE/WebSocket 기반 실시간 알림 시스템', 2, 'HARD', 50, 'Spring Boot, WebSocket, Redis Pub/Sub', NULL);

-- 풀스택 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('개인 블로그 플랫폼', '마크다운 에디터와 댓글 기능이 있는 블로그', 3, 'MEDIUM', 50, 'Next.js, Spring Boot, PostgreSQL', NULL),
('설문조사 플랫폼', '설문 생성, 응답 수집, 통계 시각화 기능', 3, 'MEDIUM', 45, 'Vue.js, Node.js, MongoDB', NULL),
('이커머스 쇼핑몰', '상품 관리, 장바구니, 결제 기능이 있는 쇼핑몰', 3, 'HARD', 80, 'React, Spring Boot, MySQL, Redis', NULL),
('실시간 협업 에디터', '구글 독스 스타일의 실시간 문서 편집기', 3, 'HARD', 100, 'React, WebSocket, CRDT, PostgreSQL', NULL),
('소셜 미디어 클론', '게시물, 팔로우, 좋아요 기능의 SNS', 3, 'HARD', 120, 'Next.js, NestJS, PostgreSQL, Redis', NULL);

-- 모바일 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('메모 앱', '간단한 메모 작성 및 관리 앱', 4, 'EASY', 15, 'React Native, AsyncStorage', NULL),
('습관 트래커', '매일 습관을 기록하고 통계를 보여주는 앱', 4, 'MEDIUM', 30, 'Flutter, SQLite, Provider', NULL),
('운동 기록 앱', '운동 루틴과 기록을 관리하는 피트니스 앱', 4, 'MEDIUM', 40, 'React Native, Firebase', NULL),
('가계부 앱', '수입/지출 기록 및 월별 리포트 생성 앱', 4, 'MEDIUM', 35, 'Flutter, Hive, fl_chart', NULL),
('위치 기반 알림 앱', '특정 장소에 도착하면 알림을 보내는 앱', 4, 'HARD', 50, 'React Native, Geofencing, Firebase', NULL);

-- CLI 도구 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('파일 정리 도구', '파일을 확장자별로 자동 분류하는 CLI 도구', 6, 'EASY', 8, 'Python, Click', NULL),
('Git 커밋 템플릿 생성기', '커밋 메시지 규칙에 맞는 템플릿 생성 도구', 6, 'EASY', 10, 'Node.js, Inquirer', NULL),
('Markdown to PDF 변환기', '마크다운 파일을 PDF로 변환하는 도구', 6, 'MEDIUM', 15, 'Python, WeasyPrint', NULL),
('프로젝트 스캐폴딩 도구', '보일러플레이트 프로젝트를 생성하는 CLI', 6, 'MEDIUM', 25, 'Node.js, Yeoman', NULL),
('데이터베이스 마이그레이션 도구', 'DB 스키마 버전 관리 CLI 도구', 6, 'HARD', 40, 'Go, cobra, database/sql', NULL);

-- 데이터/AI 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('감정 분석 API', '텍스트의 감정을 분석하는 API', 7, 'MEDIUM', 30, 'Python, FastAPI, Transformers', NULL),
('이미지 분류 서비스', '업로드된 이미지를 분류하는 웹 서비스', 7, 'MEDIUM', 35, 'Python, TensorFlow, Flask', NULL),
('추천 시스템', '협업 필터링 기반 상품 추천 시스템', 7, 'HARD', 50, 'Python, scikit-learn, Surprise', NULL),
('챗봇 서비스', 'FAQ 자동 응답 챗봇', 7, 'HARD', 60, 'Python, LangChain, OpenAI API', NULL),
('실시간 데이터 파이프라인', 'Kafka 기반 실시간 데이터 처리 파이프라인', 7, 'HARD', 70, 'Python, Kafka, Spark', NULL);

-- 게임 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('숫자 야구 게임', '터미널에서 플레이하는 숫자 맞추기 게임', 8, 'EASY', 5, 'Python or JavaScript', NULL),
('스네이크 게임', '클래식 스네이크 게임 웹 버전', 8, 'EASY', 10, 'JavaScript, Canvas', NULL),
('타이핑 게임', '타이핑 속도를 측정하는 게임', 8, 'MEDIUM', 20, 'React, TypeScript', NULL),
('2D 플랫포머', 'Unity로 만드는 간단한 플랫포머 게임', 8, 'MEDIUM', 40, 'Unity, C#', NULL),
('멀티플레이어 퀴즈 게임', '실시간 대전 퀴즈 게임', 8, 'HARD', 60, 'Node.js, Socket.io, React', NULL);

-- DevOps 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('Docker 이미지 빌더', 'Dockerfile을 자동 생성하는 도구', 9, 'EASY', 15, 'Python, Docker SDK', NULL),
('CI/CD 파이프라인', 'GitHub Actions 기반 자동 배포 파이프라인', 9, 'MEDIUM', 20, 'GitHub Actions, Docker', NULL),
('서버 모니터링 대시보드', 'Prometheus + Grafana 기반 모니터링 시스템', 9, 'MEDIUM', 30, 'Prometheus, Grafana, Docker Compose', NULL),
('Kubernetes 배포 자동화', 'Helm 차트를 활용한 K8s 배포 도구', 9, 'HARD', 50, 'Kubernetes, Helm, ArgoCD', NULL),
('인프라 코드 템플릿', 'Terraform으로 AWS 인프라를 구성하는 템플릿', 9, 'HARD', 45, 'Terraform, AWS', NULL);

-- API/라이브러리 아이디어
INSERT IGNORE INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
('날짜/시간 유틸리티 라이브러리', '다양한 날짜 포맷팅 기능을 제공하는 라이브러리', 10, 'EASY', 10, 'TypeScript', NULL),
('유효성 검사 라이브러리', '폼 데이터 유효성 검사 라이브러리', 10, 'MEDIUM', 20, 'TypeScript', NULL),
('HTTP 클라이언트 래퍼', 'Axios를 감싼 타입 안전한 HTTP 클라이언트', 10, 'MEDIUM', 25, 'TypeScript, Axios', NULL),
('상태 관리 라이브러리', '간단한 상태 관리 라이브러리 구현', 10, 'HARD', 40, 'TypeScript', NULL),
('ORM 라이브러리', '간단한 ORM 라이브러리 직접 구현', 10, 'HARD', 60, 'TypeScript, SQL', NULL);

-- 아이디어-태그 연결 (일부 예시)
INSERT IGNORE INTO idea_tags (idea_id, tag_id) VALUES
(1, 1), (1, 2),  -- Todo List: JavaScript, TypeScript
(2, 9),          -- 날씨 대시보드: React
(3, 9), (3, 2),  -- 포트폴리오: React, TypeScript
(6, 13), (6, 18), -- URL 단축기: Spring Boot, MySQL
(11, 12), (11, 13), -- 블로그: Next.js, Spring Boot
(21, 3),         -- 파일 정리: Python
(26, 3), (26, 16); -- 감정 분석: Python, FastAPI
