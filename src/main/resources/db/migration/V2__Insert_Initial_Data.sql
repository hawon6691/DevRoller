-- =====================================================
-- DevRoller Initial Data
-- V2: Categories, Tags, Achievements, Titles, Ideas
-- =====================================================

-- -----------------------------------------------------
-- Categories (카테고리)
-- -----------------------------------------------------
INSERT INTO categories (name, description, icon, display_order) VALUES
('웹 개발', '웹 애플리케이션, 웹사이트 개발 프로젝트', '🌐', 1),
('모바일 앱', 'iOS, Android 모바일 앱 개발', '📱', 2),
('CLI 도구', '커맨드라인 인터페이스 도구 개발', '💻', 3),
('게임', '게임 개발 프로젝트', '🎮', 4),
('AI/ML', '인공지능, 머신러닝 프로젝트', '🤖', 5),
('데이터', '데이터 분석, 시각화 프로젝트', '📊', 6),
('자동화', '업무 자동화, 봇 개발', '⚙️', 7),
('API/백엔드', 'REST API, 백엔드 서버 개발', '🔌', 8),
('데스크톱', '데스크톱 애플리케이션 개발', '🖥️', 9),
('기타', '기타 개발 프로젝트', '📦', 10);

-- -----------------------------------------------------
-- Tags (태그)
-- -----------------------------------------------------
INSERT INTO tags (name, color) VALUES
-- 기술 스택
('Spring Boot', '#6DB33F'),
('React', '#61DAFB'),
('Vue.js', '#4FC08D'),
('Node.js', '#339933'),
('Python', '#3776AB'),
('Java', '#ED8B00'),
('TypeScript', '#3178C6'),
('Go', '#00ADD8'),
('Rust', '#000000'),
('Flutter', '#02569B'),
-- 특성
('REST API', '#FF6B6B'),
('GraphQL', '#E535AB'),
('실시간', '#FFD93D'),
('CRUD', '#4ECDC4'),
('인증', '#845EC2'),
('파일처리', '#FF9671'),
('크롤링', '#FFC75F'),
('챗봇', '#F9F871'),
('대시보드', '#00C9A7'),
('알고리즘', '#C34A36'),
-- 난이도/규모
('입문자용', '#98D8C8'),
('포트폴리오', '#F67280'),
('사이드프로젝트', '#355C7D'),
('1일완성', '#6C5B7B'),
('주말프로젝트', '#C06C84');

-- -----------------------------------------------------
-- Achievements (업적)
-- -----------------------------------------------------
INSERT INTO achievements (code, name, description, icon, type, required_value, reward_exp, is_hidden, display_order) VALUES
-- 완료 횟수 업적
('FIRST_COMPLETE', '첫 발걸음', '첫 번째 프로젝트를 완료했습니다!', '🎉', 'COMPLETE_COUNT', 1, 50, FALSE, 1),
('COMPLETE_5', '꾸준한 개발자', '5개의 프로젝트를 완료했습니다.', '⭐', 'COMPLETE_COUNT', 5, 100, FALSE, 2),
('COMPLETE_10', '열정적인 개발자', '10개의 프로젝트를 완료했습니다.', '🌟', 'COMPLETE_COUNT', 10, 200, FALSE, 3),
('COMPLETE_25', '프로젝트 마스터', '25개의 프로젝트를 완료했습니다.', '💫', 'COMPLETE_COUNT', 25, 500, FALSE, 4),
('COMPLETE_50', '레전드 개발자', '50개의 프로젝트를 완료했습니다!', '🏆', 'COMPLETE_COUNT', 50, 1000, FALSE, 5),
('COMPLETE_100', '개발의 신', '100개의 프로젝트를 완료했습니다!!', '👑', 'COMPLETE_COUNT', 100, 2000, TRUE, 6),

-- 스트릭 업적
('STREAK_3', '3일 연속', '3일 연속으로 활동했습니다.', '🔥', 'STREAK', 3, 30, FALSE, 10),
('STREAK_7', '일주일 연속', '7일 연속으로 활동했습니다!', '🔥', 'STREAK', 7, 70, FALSE, 11),
('STREAK_14', '2주 연속', '14일 연속으로 활동했습니다!', '🔥', 'STREAK', 14, 150, FALSE, 12),
('STREAK_30', '한 달 연속', '30일 연속으로 활동했습니다!!', '🔥', 'STREAK', 30, 300, FALSE, 13),
('STREAK_100', '100일 연속', '100일 연속 활동! 대단합니다!', '🔥', 'STREAK', 100, 1000, TRUE, 14),

-- 카테고리 업적
('WEB_MASTER', '웹 마스터', '웹 개발 프로젝트 10개 완료', '🌐', 'CATEGORY', 10, 200, FALSE, 20),
('MOBILE_MASTER', '모바일 마스터', '모바일 앱 프로젝트 10개 완료', '📱', 'CATEGORY', 10, 200, FALSE, 21),
('CLI_MASTER', 'CLI 마스터', 'CLI 도구 프로젝트 10개 완료', '💻', 'CATEGORY', 10, 200, FALSE, 22),
('GAME_MASTER', '게임 마스터', '게임 프로젝트 10개 완료', '🎮', 'CATEGORY', 10, 200, FALSE, 23),
('AI_MASTER', 'AI 마스터', 'AI/ML 프로젝트 10개 완료', '🤖', 'CATEGORY', 10, 200, FALSE, 24),
('FULL_STACK', '풀스택 개발자', '모든 카테고리에서 1개 이상 완료', '🎯', 'CATEGORY', 10, 500, FALSE, 25),

-- 난이도 업적
('EASY_10', '쉬운 건 식은 죽 먹기', 'EASY 난이도 10개 완료', '🟢', 'DIFFICULTY', 10, 100, FALSE, 30),
('MEDIUM_10', '적당한 도전', 'MEDIUM 난이도 10개 완료', '🟡', 'DIFFICULTY', 10, 150, FALSE, 31),
('HARD_10', '진정한 도전자', 'HARD 난이도 10개 완료', '🔴', 'DIFFICULTY', 10, 300, FALSE, 32),
('HARD_ONLY_5', '하드코어', 'HARD만 5개 연속 완료', '💀', 'DIFFICULTY', 5, 500, TRUE, 33),

-- 특별 업적
('FIRST_PICK', '운명의 선택', '첫 번째 추첨을 했습니다!', '🎲', 'SPECIAL', 1, 10, FALSE, 40),
('PICK_100', '추첨 중독', '100번의 추첨을 했습니다.', '🎰', 'SPECIAL', 100, 200, FALSE, 41),
('FIRST_REVIEW', '평론가 데뷔', '첫 번째 리뷰를 작성했습니다.', '📝', 'SPECIAL', 1, 20, FALSE, 42),
('REVIEW_10', '리뷰 마스터', '10개의 리뷰를 작성했습니다.', '✍️', 'SPECIAL', 10, 100, FALSE, 43),
('SUGGESTION_APPROVED', '아이디어 뱅크', '제안한 주제가 승인되었습니다!', '💡', 'SPECIAL', 1, 200, FALSE, 44),
('LEVEL_10', '레벨 10 달성', '레벨 10에 도달했습니다!', '🎖️', 'SPECIAL', 1, 0, FALSE, 45),
('LEVEL_25', '레벨 25 달성', '레벨 25에 도달했습니다!', '🏅', 'SPECIAL', 1, 0, FALSE, 46),
('LEVEL_50', '레벨 50 달성', '레벨 50에 도달했습니다!!', '🥇', 'SPECIAL', 1, 0, TRUE, 47);

-- -----------------------------------------------------
-- Titles (칭호)
-- -----------------------------------------------------
INSERT INTO titles (code, name, description, type, required_level, required_achievement_code, rarity, display_order) VALUES
-- 레벨 칭호
('NOVICE', '초보 개발자', '개발의 세계에 첫 발을 내딛은 자', 'LEVEL', 1, NULL, 'COMMON', 1),
('APPRENTICE', '견습 개발자', '꾸준히 성장하는 개발자', 'LEVEL', 5, NULL, 'COMMON', 2),
('DEVELOPER', '개발자', '인정받는 개발자', 'LEVEL', 10, NULL, 'UNCOMMON', 3),
('SENIOR', '시니어 개발자', '경험이 풍부한 개발자', 'LEVEL', 20, NULL, 'UNCOMMON', 4),
('EXPERT', '전문가', '분야의 전문가', 'LEVEL', 30, NULL, 'RARE', 5),
('MASTER', '마스터', '개발의 달인', 'LEVEL', 40, NULL, 'RARE', 6),
('GRANDMASTER', '그랜드마스터', '전설적인 개발자', 'LEVEL', 50, NULL, 'EPIC', 7),

-- 업적 칭호
('FIRST_STEP', '첫 걸음마', '첫 프로젝트를 완료한 자', 'ACHIEVEMENT', NULL, 'FIRST_COMPLETE', 'COMMON', 10),
('PROJECT_HUNTER', '프로젝트 헌터', '10개의 프로젝트를 정복한 자', 'ACHIEVEMENT', NULL, 'COMPLETE_10', 'UNCOMMON', 11),
('PROJECT_LEGEND', '프로젝트 레전드', '50개의 프로젝트를 정복한 자', 'ACHIEVEMENT', NULL, 'COMPLETE_50', 'EPIC', 12),
('GOD_OF_DEV', '개발의 신', '100개의 프로젝트를 완료한 신', 'ACHIEVEMENT', NULL, 'COMPLETE_100', 'LEGENDARY', 13),

('FLAME_KEEPER', '불꽃 수호자', '7일 연속 활동한 자', 'ACHIEVEMENT', NULL, 'STREAK_7', 'UNCOMMON', 20),
('ETERNAL_FLAME', '영원한 불꽃', '30일 연속 활동한 자', 'ACHIEVEMENT', NULL, 'STREAK_30', 'RARE', 21),
('UNDYING_FLAME', '꺼지지 않는 불꽃', '100일 연속 활동한 전설', 'ACHIEVEMENT', NULL, 'STREAK_100', 'LEGENDARY', 22),

('WEB_WARRIOR', '웹 전사', '웹 개발의 달인', 'ACHIEVEMENT', NULL, 'WEB_MASTER', 'RARE', 30),
('MOBILE_WARRIOR', '모바일 전사', '모바일 개발의 달인', 'ACHIEVEMENT', NULL, 'MOBILE_MASTER', 'RARE', 31),
('GAME_WARRIOR', '게임 전사', '게임 개발의 달인', 'ACHIEVEMENT', NULL, 'GAME_MASTER', 'RARE', 32),
('AI_WARRIOR', 'AI 전사', 'AI 개발의 달인', 'ACHIEVEMENT', NULL, 'AI_MASTER', 'RARE', 33),
('OMNIPOTENT', '전지전능', '모든 분야를 섭렵한 자', 'ACHIEVEMENT', NULL, 'FULL_STACK', 'EPIC', 34),

('CHALLENGER', '도전자', 'HARD 난이도를 정복한 자', 'ACHIEVEMENT', NULL, 'HARD_10', 'RARE', 40),
('HARDCORE', '하드코어', '오직 어려움만을 추구하는 자', 'ACHIEVEMENT', NULL, 'HARD_ONLY_5', 'EPIC', 41),

('IDEA_MAKER', '아이디어 뱅크', '새로운 아이디어를 제시한 자', 'ACHIEVEMENT', NULL, 'SUGGESTION_APPROVED', 'UNCOMMON', 50),
('CRITIC', '평론가', '날카로운 리뷰를 남기는 자', 'ACHIEVEMENT', NULL, 'REVIEW_10', 'UNCOMMON', 51);

-- -----------------------------------------------------
-- Ideas (샘플 아이디어) - 웹 개발
-- -----------------------------------------------------
INSERT INTO ideas (title, description, category_id, difficulty, estimated_hours, tech_stack, reference_url) VALUES
-- 웹 개발 (category_id = 1)
('투두 리스트 앱', '할 일을 관리할 수 있는 기본적인 투두 리스트 웹 애플리케이션입니다. CRUD 기능과 localStorage를 활용한 데이터 저장을 구현합니다.', 1, 'EASY', 4, 'HTML, CSS, JavaScript, localStorage', NULL),
('개인 포트폴리오 사이트', '자신을 소개하고 프로젝트를 보여줄 수 있는 반응형 포트폴리오 웹사이트입니다.', 1, 'EASY', 8, 'HTML, CSS, JavaScript', NULL),
('날씨 정보 앱', '외부 API를 활용하여 날씨 정보를 보여주는 웹 애플리케이션입니다.', 1, 'EASY', 6, 'React, OpenWeather API, Axios', NULL),
('블로그 플랫폼', '마크다운을 지원하는 개인 블로그 플랫폼입니다. 글 작성, 수정, 삭제 기능을 포함합니다.', 1, 'MEDIUM', 20, 'Next.js, MDX, Tailwind CSS', NULL),
('실시간 채팅 앱', 'WebSocket을 활용한 실시간 채팅 애플리케이션입니다.', 1, 'MEDIUM', 16, 'React, Socket.io, Node.js, Express', NULL),
('이커머스 쇼핑몰', '상품 목록, 장바구니, 결제 기능을 갖춘 쇼핑몰입니다.', 1, 'HARD', 40, 'React, Spring Boot, MySQL, Redis', NULL),
('소셜 미디어 클론', '트위터/인스타그램 스타일의 소셜 미디어 플랫폼입니다.', 1, 'HARD', 60, 'React, Node.js, MongoDB, AWS S3', NULL),

-- 모바일 앱 (category_id = 2)
('계산기 앱', '기본적인 사칙연산이 가능한 계산기 앱입니다.', 2, 'EASY', 4, 'Flutter, Dart', NULL),
('메모 앱', '간단한 메모를 작성하고 저장할 수 있는 앱입니다.', 2, 'EASY', 8, 'React Native, AsyncStorage', NULL),
('습관 추적 앱', '일일 습관을 추적하고 통계를 보여주는 앱입니다.', 2, 'MEDIUM', 20, 'Flutter, SQLite, Charts', NULL),
('운동 기록 앱', '운동 루틴과 기록을 관리하는 피트니스 앱입니다.', 2, 'MEDIUM', 24, 'React Native, Firebase', NULL),
('가계부 앱', '수입/지출을 관리하고 분석해주는 가계부 앱입니다.', 2, 'HARD', 32, 'Flutter, Provider, Hive, Charts', NULL),

-- CLI 도구 (category_id = 3)
('파일 정리 스크립트', '폴더 내 파일을 확장자별로 자동 정리하는 CLI 도구입니다.', 3, 'EASY', 2, 'Python, os, shutil', NULL),
('Git 커밋 메시지 생성기', 'AI를 활용하여 Git 커밋 메시지를 자동 생성하는 도구입니다.', 3, 'MEDIUM', 8, 'Node.js, OpenAI API, Commander.js', NULL),
('마크다운 변환기', '마크다운을 HTML/PDF로 변환하는 CLI 도구입니다.', 3, 'MEDIUM', 10, 'Go, Goldmark, Cobra', NULL),
('프로젝트 보일러플레이트 생성기', '프로젝트 초기 설정을 자동화하는 CLI 도구입니다.', 3, 'HARD', 16, 'Rust, Clap, Handlebars', NULL),

-- 게임 (category_id = 4)
('숫자 맞추기 게임', '컴퓨터가 생각한 숫자를 맞추는 간단한 게임입니다.', 4, 'EASY', 2, 'Python', NULL),
('틱택토 게임', '2인용 틱택토 게임입니다.', 4, 'EASY', 4, 'JavaScript, HTML Canvas', NULL),
('스네이크 게임', '클래식 스네이크 게임입니다.', 4, 'MEDIUM', 8, 'JavaScript, HTML Canvas', NULL),
('테트리스', '클래식 테트리스 게임입니다.', 4, 'MEDIUM', 12, 'JavaScript, HTML Canvas', NULL),
('2D 플랫폼 게임', '간단한 2D 횡스크롤 플랫폼 게임입니다.', 4, 'HARD', 30, 'Unity, C#', NULL),

-- AI/ML (category_id = 5)
('감정 분석기', '텍스트의 감정을 분석하는 간단한 모델입니다.', 5, 'EASY', 6, 'Python, scikit-learn, NLTK', NULL),
('이미지 분류기', '이미지를 분류하는 딥러닝 모델입니다.', 5, 'MEDIUM', 12, 'Python, TensorFlow, Keras', NULL),
('챗봇', 'GPT API를 활용한 대화형 챗봇입니다.', 5, 'MEDIUM', 10, 'Python, OpenAI API, FastAPI', NULL),
('추천 시스템', '협업 필터링 기반의 추천 시스템입니다.', 5, 'HARD', 24, 'Python, Surprise, Pandas', NULL),

-- 데이터 (category_id = 6)
('엑셀 데이터 분석 도구', '엑셀 파일을 분석하고 리포트를 생성합니다.', 6, 'EASY', 6, 'Python, Pandas, openpyxl', NULL),
('대시보드', '데이터를 시각화하는 인터랙티브 대시보드입니다.', 6, 'MEDIUM', 16, 'Python, Streamlit, Plotly', NULL),
('웹 스크래퍼', '웹사이트에서 데이터를 수집하는 크롤러입니다.', 6, 'MEDIUM', 10, 'Python, BeautifulSoup, Selenium', NULL),

-- 자동화 (category_id = 7)
('이메일 자동 발송기', '템플릿 기반 이메일을 자동 발송합니다.', 7, 'EASY', 4, 'Python, smtplib, Jinja2', NULL),
('슬랙 봇', '슬랙 워크스페이스용 자동화 봇입니다.', 7, 'MEDIUM', 12, 'Node.js, Bolt, Slack API', NULL),
('GitHub Actions 자동화', 'CI/CD 파이프라인을 구축합니다.', 7, 'MEDIUM', 8, 'GitHub Actions, Docker', NULL),

-- API/백엔드 (category_id = 8)
('URL 단축 서비스', 'URL을 단축하고 클릭 통계를 제공합니다.', 8, 'EASY', 6, 'Node.js, Express, MongoDB', NULL),
('인증 서버', 'JWT 기반 인증/인가 서버입니다.', 8, 'MEDIUM', 12, 'Spring Boot, JWT, MySQL', NULL),
('파일 업로드 서버', '파일 업로드/다운로드 API 서버입니다.', 8, 'MEDIUM', 10, 'Go, Gin, MinIO', NULL),
('GraphQL API 서버', 'GraphQL 기반 API 서버입니다.', 8, 'HARD', 20, 'Node.js, Apollo Server, PostgreSQL', NULL);

-- -----------------------------------------------------
-- Idea Tags (아이디어-태그 연결)
-- -----------------------------------------------------
-- 투두 리스트 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (1, 14), (1, 23);  -- CRUD, 입문자용
-- 개인 포트폴리오 사이트
INSERT INTO idea_tags (idea_id, tag_id) VALUES (2, 22), (2, 23);  -- 포트폴리오, 입문자용
-- 날씨 정보 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (3, 2), (3, 11);  -- React, REST API
-- 블로그 플랫폼
INSERT INTO idea_tags (idea_id, tag_id) VALUES (4, 2), (4, 14), (4, 22);  -- React, CRUD, 포트폴리오
-- 실시간 채팅 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (5, 2), (5, 4), (5, 13);  -- React, Node.js, 실시간
-- 이커머스 쇼핑몰
INSERT INTO idea_tags (idea_id, tag_id) VALUES (6, 2), (6, 1), (6, 11), (6, 15);  -- React, Spring Boot, REST API, 인증
-- 소셜 미디어 클론
INSERT INTO idea_tags (idea_id, tag_id) VALUES (7, 2), (7, 4), (7, 13), (7, 15);  -- React, Node.js, 실시간, 인증
-- 계산기 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (8, 10), (8, 23);  -- Flutter, 입문자용
-- 메모 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (9, 2), (9, 14), (9, 23);  -- React (Native), CRUD, 입문자용
-- 습관 추적 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (10, 10), (10, 19);  -- Flutter, 대시보드
-- 운동 기록 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (11, 2), (11, 15);  -- React (Native), 인증
-- 가계부 앱
INSERT INTO idea_tags (idea_id, tag_id) VALUES (12, 10), (12, 19);  -- Flutter, 대시보드
-- 파일 정리 스크립트
INSERT INTO idea_tags (idea_id, tag_id) VALUES (13, 5), (13, 16), (13, 24);  -- Python, 파일처리, 1일완성
-- Git 커밋 메시지 생성기
INSERT INTO idea_tags (idea_id, tag_id) VALUES (14, 4), (14, 18);  -- Node.js, 챗봇
-- 마크다운 변환기
INSERT INTO idea_tags (idea_id, tag_id) VALUES (15, 8), (15, 16);  -- Go, 파일처리
-- 프로젝트 보일러플레이트 생성기
INSERT INTO idea_tags (idea_id, tag_id) VALUES (16, 9), (16, 16);  -- Rust, 파일처리
-- 숫자 맞추기 게임
INSERT INTO idea_tags (idea_id, tag_id) VALUES (17, 5), (17, 23), (17, 24);  -- Python, 입문자용, 1일완성
-- 틱택토 게임
INSERT INTO idea_tags (idea_id, tag_id) VALUES (18, 20), (18, 23);  -- 알고리즘, 입문자용
-- 스네이크 게임
INSERT INTO idea_tags (idea_id, tag_id) VALUES (19, 20), (19, 25);  -- 알고리즘, 주말프로젝트
-- 테트리스
INSERT INTO idea_tags (idea_id, tag_id) VALUES (20, 20), (20, 25);  -- 알고리즘, 주말프로젝트
-- 2D 플랫폼 게임
INSERT INTO idea_tags (idea_id, tag_id) VALUES (21, 22);  -- 포트폴리오
-- 감정 분석기
INSERT INTO idea_tags (idea_id, tag_id) VALUES (22, 5), (22, 23);  -- Python, 입문자용
-- 이미지 분류기
INSERT INTO idea_tags (idea_id, tag_id) VALUES (23, 5);  -- Python
-- 챗봇
INSERT INTO idea_tags (idea_id, tag_id) VALUES (24, 5), (24, 18), (24, 11);  -- Python, 챗봇, REST API
-- 추천 시스템
INSERT INTO idea_tags (idea_id, tag_id) VALUES (25, 5), (25, 20);  -- Python, 알고리즘
-- 엑셀 데이터 분석 도구
INSERT INTO idea_tags (idea_id, tag_id) VALUES (26, 5), (26, 16);  -- Python, 파일처리
-- 대시보드
INSERT INTO idea_tags (idea_id, tag_id) VALUES (27, 5), (27, 19);  -- Python, 대시보드
-- 웹 스크래퍼
INSERT INTO idea_tags (idea_id, tag_id) VALUES (28, 5), (28, 17);  -- Python, 크롤링
-- 이메일 자동 발송기
INSERT INTO idea_tags (idea_id, tag_id) VALUES (29, 5), (29, 24);  -- Python, 1일완성
-- 슬랙 봇
INSERT INTO idea_tags (idea_id, tag_id) VALUES (30, 4), (30, 18), (30, 11);  -- Node.js, 챗봇, REST API
-- GitHub Actions 자동화
INSERT INTO idea_tags (idea_id, tag_id) VALUES (31, 23);  -- 사이드프로젝트
-- URL 단축 서비스
INSERT INTO idea_tags (idea_id, tag_id) VALUES (32, 4), (32, 11), (32, 14);  -- Node.js, REST API, CRUD
-- 인증 서버
INSERT INTO idea_tags (idea_id, tag_id) VALUES (33, 1), (33, 11), (33, 15);  -- Spring Boot, REST API, 인증
-- 파일 업로드 서버
INSERT INTO idea_tags (idea_id, tag_id) VALUES (34, 8), (34, 11), (34, 16);  -- Go, REST API, 파일처리
-- GraphQL API 서버
INSERT INTO idea_tags (idea_id, tag_id) VALUES (35, 4), (35, 12);  -- Node.js, GraphQL