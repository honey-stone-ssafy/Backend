CREATE SCHEMA honeystone;
USE honeystone;

-- 1. 계획(일정) 테이블
CREATE TABLE plans (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  title       VARCHAR(50)  NULL,
  scheduled_at TIMESTAMP    NOT NULL COMMENT '시간까지',
  content     VARCHAR(255) NULL,
  scope       ENUM('ALL','FRIENDS','PRIVATE') NOT NULL COMMENT '전체/친구/비공개',
  PRIMARY KEY (id)
);

-- 2. 동영상 테이블
CREATE TABLE videos (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  title       VARCHAR(50)  NOT NULL,
  description VARCHAR(255) NULL,
  level       ENUM('BEGINNER','INTERMEDIATE','ADVANCED') NOT NULL COMMENT '홀드 색깔',
  skill       ENUM('TECHNIQUE1','TECHNIQUE2','OTHER') NOT NULL COMMENT '기술 명/기타',
  PRIMARY KEY (id)
);

-- 3. 즐겨찾기 테이블 (user ↔ video)
CREATE TABLE favorites (
  id       BIGINT NOT NULL AUTO_INCREMENT,
  user_id  BIGINT NOT NULL,
  video_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id)  REFERENCES users(id),
  FOREIGN KEY (video_id) REFERENCES videos(id)
);

-- 4. 리뷰 테이블
CREATE TABLE reviews (
  id           BIGINT    NOT NULL AUTO_INCREMENT,
  content      VARCHAR(255) NOT NULL,
  created_at   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NULL,
  deleted_at   TIMESTAMP NULL,
  user_id      BIGINT    NOT NULL,
  video_id     BIGINT    NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id)  REFERENCES users(id),
  FOREIGN KEY (video_id) REFERENCES videos(id)
);

-- 5. 클라이밍 위치 테이블
CREATE TABLE climbing_locations (
  id          BIGINT      NOT NULL AUTO_INCREMENT,
  name        VARCHAR(50) NOT NULL,
  wall        VARCHAR(50) NULL,
  color       VARCHAR(50) NOT NULL,
  video_id    BIGINT      NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (video_id) REFERENCES videos(id)
);

-- 6. 사용자-계획 매핑 테이블 (요청 상태 포함)
CREATE TABLE user_plans (
  id       BIGINT      NOT NULL AUTO_INCREMENT,
  status   ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL COMMENT '요청 상태',
  user_id  BIGINT      NOT NULL,
  plan_id  BIGINT      NOT NULL,
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
  video_id  BIGINT      NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (video_id) REFERENCES videos(id)
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
