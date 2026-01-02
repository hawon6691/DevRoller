-- =====================================================
-- DevRoller Likes and Hidden Features
-- V5: Add idea_likes and hidden_ideas tables
-- =====================================================

-- 아이디어 좋아요 테이블
CREATE TABLE IF NOT EXISTS idea_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    idea_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_idea_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_idea_like_idea FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE CASCADE,
    CONSTRAINT uk_idea_likes UNIQUE (user_id, idea_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 숨긴 아이디어 테이블
CREATE TABLE IF NOT EXISTS hidden_ideas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    idea_id BIGINT NOT NULL,
    reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_hidden_idea_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_hidden_idea_idea FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE CASCADE,
    CONSTRAINT uk_hidden_ideas UNIQUE (user_id, idea_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 인덱스 추가
CREATE INDEX idx_idea_likes_user_id ON idea_likes(user_id);
CREATE INDEX idx_idea_likes_idea_id ON idea_likes(idea_id);
CREATE INDEX idx_hidden_ideas_user_id ON hidden_ideas(user_id);
CREATE INDEX idx_hidden_ideas_idea_id ON hidden_ideas(idea_id);
