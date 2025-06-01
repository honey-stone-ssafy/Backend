CREATE SCHEMA honeystone;
USE honeystone;

-- 1. 계획(일정) 테이블
CREATE TABLE plans (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  title       VARCHAR(50)  NULL,
  start       TIMESTAMP    NOT NULL COMMENT '시간까지',
  end         TIMESTAMP    NOT NULL COMMENT '시간까지',
  memo     VARCHAR(255) NULL,
  location    VARCHAR(50) NULL,
  scope       ENUM('ALL','FRIENDS','PRIVATE') NOT NULL COMMENT '전체/친구/비공개',
  created_at  TIMESTAMP NOT NULL,
  updated_at  TIMESTAMP NULL,
  deleted_at  TIMESTAMP NULL,
  user_id     BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id)  REFERENCES users(id)
);

-- 2. 게시판 테이블
CREATE TABLE boards (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  title       VARCHAR(50)  NOT NULL,
  description VARCHAR(255) NULL,
  level       ENUM('WHITE','YELLOW','ORANGE', 'GREEN', 'BLUE', 'RED', 'PURPLE', 'GREY', 'BROWN', 'BLACK') NOT NULL COMMENT '난이도/테이프 색깔',
  skill SET(
    'PINCH',
    'SLOPER',
    'CRIMP',
    'POCKET_HOLD',
    'COORDINATION',
    'LUNGE',
    'DYNO',
    'BALANCE',
    'OVERHANG',
    'TOE_HOOK',
    'HEEL_HOOK',
    'BAT_HANG',
    'COUNTER_BALANCING',
    'DEADPOINT',
    'POGO',
    'FLIP',
    'KNEEBAR',
    'DROP_KNEE',
    'PUSH',
    'RUN_AND_JUMP',
    'CAMPUSING',
    'TOE_CATCH'
  ) NULL COMMENT '기술 명',
  hold_color       VARCHAR(50) NULL,
  user_id  BIGINT NOT NULL,
  created_at   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NULL,
  deleted_at   TIMESTAMP NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id)  REFERENCES users(id)
);

-- 3. 즐겨찾기 테이블 (user ↔ board)
CREATE TABLE favorites (
  id       BIGINT NOT NULL AUTO_INCREMENT,
  user_id  BIGINT NOT NULL,
  board_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (user_id, board_id),
  FOREIGN KEY (user_id)  REFERENCES users(id),
  FOREIGN KEY (board_id) REFERENCES boards(id)
);

-- 4. 리뷰 테이블
CREATE TABLE reviews (
  id           BIGINT    NOT NULL AUTO_INCREMENT,
  content      VARCHAR(255) NOT NULL,
  created_at   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NULL,
  deleted_at   TIMESTAMP NULL,
  user_id      BIGINT    NOT NULL,
  board_id     BIGINT    NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id)  REFERENCES users(id),
  FOREIGN KEY (board_id) REFERENCES boards(id)
);

-- 5. 더클라임 지점 테이블
    CREATE TABLE the_climb (
      id          BIGINT      NOT NULL AUTO_INCREMENT,
      location        VARCHAR(50) NOT NULL,
      wall        VARCHAR(50) NULL,
      PRIMARY KEY (id)
    );
	
-- 6. 사용자-계획 매핑 테이블 (요청 상태 포함)
CREATE TABLE request_plans (
  id       BIGINT      NOT NULL AUTO_INCREMENT,
  status   ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL COMMENT '요청 상태',
  user_id  BIGINT      NOT NULL,
  plan_id  BIGINT      NOT NULL,
  role ENUM('OWNER', 'PARTICIPANT') NOT NULL DEFAULT 'PARTICIPANT',
  created_at   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NULL,
  deleted_at   TIMESTAMP NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (plan_id) REFERENCES plans(id)
);

-- 7. 사용자 파일 테이블
CREATE TABLE user_files (
  id        BIGINT      NOT NULL AUTO_INCREMENT,
  user_id   BIGINT      NOT NULL,
  url       VARCHAR(255) NOT NULL,
  filename  VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 8. 팔로우 테이블
CREATE TABLE follows (
  id             BIGINT      NOT NULL AUTO_INCREMENT,
  created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted_at     TIMESTAMP   NULL,
  follower_id    BIGINT      NOT NULL COMMENT '요청자',
  followee_id    BIGINT      NOT NULL COMMENT '피요청자',
  PRIMARY KEY (id),
  UNIQUE KEY (follower_id, followee_id),
  FOREIGN KEY (follower_id) REFERENCES users(id),
  FOREIGN KEY (followee_id) REFERENCES users(id)
);

-- 9. 사용자 테이블
CREATE TABLE users (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  email          VARCHAR(50)  NOT NULL,
  nickname       VARCHAR(50)  NOT NULL UNIQUE COMMENT '닉네임',
  password       VARCHAR(255) NOT NULL,
  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP    NULL,
  deleted_at     TIMESTAMP    NULL,
  description    VARCHAR(255) NULL,
  img     VARCHAR(255) NOT NULL DEFAULT 'default.png' COMMENT '프로필 이미지',
  PRIMARY KEY (id)
);

-- 10. 파일 테이블
CREATE TABLE files (
  id        BIGINT      NOT NULL AUTO_INCREMENT,
  url       VARCHAR(255) NOT NULL,
  filename  VARCHAR(255) NOT NULL,
  board_id  BIGINT      NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (board_id) REFERENCES boards(id)
);

-- 11. 알람(알림) 테이블
CREATE TABLE alarms (
  id             BIGINT     NOT NULL AUTO_INCREMENT,
  type           ENUM('FOLLOW','PLAN') NOT NULL COMMENT '알람 타입',
  related_id     BIGINT     NOT NULL COMMENT '연관 리소스 ID',
  created_at     TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted_at     TIMESTAMP  NULL,
  is_responded   TINYINT(1) NOT NULL DEFAULT 0 COMMENT '응답 여부',
  user_id        BIGINT     NOT NULL COMMENT '수신자',
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 12. 리프레시 토큰 테이블
CREATE TABLE refresh_tokens (
  id           BIGINT       NOT NULL AUTO_INCREMENT,
  user_id      BIGINT       NOT NULL,
  token        VARCHAR(500) NOT NULL COMMENT 'JWT 리프레시 토큰',
  created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expired_at   TIMESTAMP    NULL COMMENT '만료 시각',
  PRIMARY KEY (id),
  UNIQUE KEY (token),  -- 하나의 토큰은 단 한 번만 유효
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 13. 더 클라임 - 게시물 매핑 테이블
CREATE TABLE the_climb_board (
	id		BIGINT  	NOT NULL AUTO_INCREMENT,
    board_id 	BIGINT 	NOT NULL,
    the_climb_id 	BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (board_id) REFERENCES boards(id),
    FOREIGN KEY (the_climb_id) REFERENCES the_climb(id)
);

