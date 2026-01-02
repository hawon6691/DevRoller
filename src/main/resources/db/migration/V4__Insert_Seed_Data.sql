-- =====================================================
-- DevRoller Initial Seed Data
-- V2: Categories, Tags, Achievements, Titles
-- =====================================================

-- -----------------------------------------------------
-- 카테고리 (Categories)
-- -----------------------------------------------------
INSERT IGNORE INTO categories (name, description, icon, display_order) VALUES
('웹 프론트엔드', 'React, Vue, Angular 등 웹 프론트엔드 프로젝트', '🌐', 1),
('웹 백엔드', 'Spring, Node.js, Django 등 웹 백엔드 프로젝트', '⚙️', 2),
('풀스택', '프론트엔드와 백엔드를 모두 포함하는 프로젝트', '🔄', 3),
('모바일', 'iOS, Android, Flutter, React Native 앱 개발', '📱', 4),
('데스크톱', 'Electron, WPF, JavaFX 등 데스크톱 애플리케이션', '🖥️', 5),
('CLI/도구', '커맨드라인 도구 및 유틸리티', '⌨️', 6),
('데이터/AI', '데이터 분석, 머신러닝, AI 프로젝트', '🤖', 7),
('게임', '게임 개발 프로젝트', '🎮', 8),
('DevOps', 'CI/CD, 인프라, 자동화 도구', '🔧', 9),
('API/라이브러리', 'API 서버, 라이브러리, SDK 개발', '📦', 10);

-- -----------------------------------------------------
-- 태그 (Tags)
-- -----------------------------------------------------
INSERT IGNORE INTO tags (name, color) VALUES
('JavaScript', '#F7DF1E'),
('TypeScript', '#3178C6'),
('Python', '#3776AB'),
('Java', '#ED8B00'),
('Kotlin', '#7F52FF'),
('Swift', '#FA7343'),
('Go', '#00ADD8'),
('Rust', '#000000'),
('React', '#61DAFB'),
('Vue', '#4FC08D'),
('Angular', '#DD0031'),
('Next.js', '#000000'),
('Spring Boot', '#6DB33F'),
('Node.js', '#339933'),
('Django', '#092E20'),
('FastAPI', '#009688'),
('PostgreSQL', '#4169E1'),
('MySQL', '#4479A1'),
('MongoDB', '#47A248'),
('Redis', '#DC382D'),
('Docker', '#2496ED'),
('Kubernetes', '#326CE5'),
('AWS', '#FF9900'),
('Firebase', '#FFCA28'),
('GraphQL', '#E10098'),
('REST API', '#009688'),
('WebSocket', '#010101'),
('OAuth', '#000000'),
('JWT', '#000000'),
('CI/CD', '#2088FF');

-- -----------------------------------------------------
-- 업적 (Achievements)
-- -----------------------------------------------------
-- 완료 횟수 기반
INSERT IGNORE INTO achievements (code, name, description, icon, type, required_value, reward_exp, is_hidden, display_order) VALUES
('FIRST_COMPLETE', '첫 번째 완료', '첫 번째 프로젝트를 완료했습니다!', '🎉', 'COMPLETE_COUNT', 1, 100, FALSE, 1),
('COMPLETE_5', '성장하는 개발자', '5개의 프로젝트를 완료했습니다', '📈', 'COMPLETE_COUNT', 5, 200, FALSE, 2),
('COMPLETE_10', '꾸준한 개발자', '10개의 프로젝트를 완료했습니다', '💪', 'COMPLETE_COUNT', 10, 300, FALSE, 3),
('COMPLETE_25', '숙련된 개발자', '25개의 프로젝트를 완료했습니다', '🏅', 'COMPLETE_COUNT', 25, 500, FALSE, 4),
('COMPLETE_50', '전문 개발자', '50개의 프로젝트를 완료했습니다', '🏆', 'COMPLETE_COUNT', 50, 1000, FALSE, 5),
('COMPLETE_100', '마스터 개발자', '100개의 프로젝트를 완료했습니다!', '👑', 'COMPLETE_COUNT', 100, 2000, FALSE, 6);

-- 스트릭 기반
INSERT IGNORE INTO achievements (code, name, description, icon, type, required_value, reward_exp, is_hidden, display_order) VALUES
('STREAK_3', '3일 연속', '3일 연속으로 프로젝트를 완료했습니다', '🔥', 'STREAK', 3, 150, FALSE, 10),
('STREAK_7', '일주일 연속', '7일 연속으로 프로젝트를 완료했습니다', '🔥🔥', 'STREAK', 7, 300, FALSE, 11),
('STREAK_14', '2주 연속', '14일 연속으로 프로젝트를 완료했습니다', '🔥🔥🔥', 'STREAK', 14, 500, FALSE, 12),
('STREAK_30', '한 달 연속', '30일 연속으로 프로젝트를 완료했습니다!', '💎', 'STREAK', 30, 1000, FALSE, 13);

-- 난이도 기반
INSERT IGNORE INTO achievements (code, name, description, icon, type, required_value, reward_exp, is_hidden, display_order) VALUES
('HARD_FIRST', '도전자', '첫 번째 HARD 난이도 프로젝트를 완료했습니다', '⚔️', 'DIFFICULTY', 1, 200, FALSE, 20),
('HARD_5', '용감한 개발자', 'HARD 난이도 프로젝트 5개를 완료했습니다', '🛡️', 'DIFFICULTY', 5, 400, FALSE, 21),
('HARD_10', '두려움 없는 개발자', 'HARD 난이도 프로젝트 10개를 완료했습니다', '⚡', 'DIFFICULTY', 10, 800, FALSE, 22);

-- 특별 업적
INSERT IGNORE INTO achievements (code, name, description, icon, type, required_value, reward_exp, is_hidden, display_order) VALUES
('EARLY_BIRD', '얼리버드', '오전 6시 이전에 프로젝트를 완료했습니다', '🌅', 'SPECIAL', 1, 100, TRUE, 30),
('NIGHT_OWL', '올빼미', '새벽 2시 이후에 프로젝트를 완료했습니다', '🦉', 'SPECIAL', 1, 100, TRUE, 31),
('WEEKEND_WARRIOR', '주말 전사', '주말에 3개의 프로젝트를 완료했습니다', '🗡️', 'SPECIAL', 3, 200, TRUE, 32);

-- -----------------------------------------------------
-- 칭호 (Titles)
-- -----------------------------------------------------
-- 레벨 기반
INSERT IGNORE INTO titles (code, name, description, type, required_level, rarity, display_order) VALUES
('LV1_BEGINNER', '입문자', '개발의 세계에 첫 발을 내딛다', 'LEVEL', 1, 'COMMON', 1),
('LV5_NOVICE', '견습 개발자', '기초를 익히기 시작하다', 'LEVEL', 5, 'COMMON', 2),
('LV10_APPRENTICE', '수습 개발자', '본격적인 성장의 시작', 'LEVEL', 10, 'UNCOMMON', 3),
('LV15_JUNIOR', '주니어 개발자', '실력이 눈에 띄기 시작하다', 'LEVEL', 15, 'UNCOMMON', 4),
('LV20_DEVELOPER', '개발자', '어엿한 개발자로 인정받다', 'LEVEL', 20, 'RARE', 5),
('LV25_SKILLED', '숙련된 개발자', '다양한 경험이 쌓이다', 'LEVEL', 25, 'RARE', 6),
('LV30_SENIOR', '시니어 개발자', '후배들의 존경을 받다', 'LEVEL', 30, 'EPIC', 7),
('LV35_EXPERT', '전문가', '해당 분야의 전문가', 'LEVEL', 35, 'EPIC', 8),
('LV40_MASTER', '마스터', '기술의 정점에 다다르다', 'LEVEL', 40, 'LEGENDARY', 9),
('LV50_LEGEND', '전설', '전설로 남을 개발자', 'LEVEL', 50, 'LEGENDARY', 10);

-- 업적 연동
INSERT IGNORE INTO titles (code, name, description, type, required_achievement_code, rarity, display_order) VALUES
('FIRST_STEP', '첫 걸음', '위대한 여정의 시작', 'ACHIEVEMENT', 'FIRST_COMPLETE', 'COMMON', 20),
('FLAME_KEEPER', '불꽃 수호자', '열정의 불꽃을 지키다', 'ACHIEVEMENT', 'STREAK_7', 'RARE', 21),
('IRON_WILL', '강철 의지', '한 달간 쉬지 않다', 'ACHIEVEMENT', 'STREAK_30', 'LEGENDARY', 22),
('CHALLENGER', '도전자', '어려운 것에 맞서다', 'ACHIEVEMENT', 'HARD_FIRST', 'UNCOMMON', 23),
('FEARLESS', '두려움 없는 자', '난관을 두려워하지 않다', 'ACHIEVEMENT', 'HARD_10', 'EPIC', 24),
('CENTURION', '백인대장', '100개의 프로젝트를 정복하다', 'ACHIEVEMENT', 'COMPLETE_100', 'LEGENDARY', 25);
