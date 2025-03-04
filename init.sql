-- 게시판 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS bookgle_board CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 데이터베이스 선택
USE bookgle_board;

-- 게시글 테이블 생성
CREATE TABLE IF NOT EXISTS board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(100) NOT NULL,
    view_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 댓글 테이블 생성
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 샘플 데이터 추가
INSERT INTO board (title, content, author) VALUES
('첫 번째 게시글입니다.', '안녕하세요! 첫 번째 게시글의 내용입니다.', '관리자'),
('두 번째 게시글입니다.', '안녕하세요! 두 번째 게시글의 내용입니다.', '사용자1'),
('세 번째 게시글입니다.', '안녕하세요! 세 번째 게시글의 내용입니다.', '사용자2'),
('네 번째 게시글입니다.', '안녕하세요! 네 번째 게시글의 내용입니다.', '사용자3'),
('다섯 번째 게시글입니다.', '안녕하세요! 다섯 번째 게시글의 내용입니다.', '사용자4'),
('여섯 번째 게시글입니다.', '안녕하세요! 여섯 번째 게시글의 내용입니다.', '사용자5'),
('일곱 번째 게시글입니다.', '안녕하세요! 일곱 번째 게시글의 내용입니다.', '사용자6'),
('여덟 번째 게시글입니다.', '안녕하세요! 여덟 번째 게시글의 내용입니다.', '사용자7'),
('아홉 번째 게시글입니다.', '안녕하세요! 아홉 번째 게시글의 내용입니다.', '사용자8'),
('열 번째 게시글입니다.', '안녕하세요! 열 번째 게시글의 내용입니다.', '사용자9'),
('열한 번째 게시글입니다.', '안녕하세요! 열한 번째 게시글의 내용입니다.', '사용자10'),
('열두 번째 게시글입니다.', '안녕하세요! 열두 번째 게시글의 내용입니다.', '사용자11');

-- 샘플 댓글 추가
INSERT INTO comment (board_id, content, author) VALUES
(1, '첫 번째 게시글에 대한 댓글입니다.', '사용자1'),
(1, '첫 번째 게시글에 대한 두 번째 댓글입니다.', '사용자2'),
(2, '두 번째 게시글에 대한 댓글입니다.', '사용자3'),
(3, '세 번째 게시글에 대한 댓글입니다.', '사용자4'); 