create schema honeystone;

use honeystone;

CREATE TABLE `Plan` (
    `plan_id` BIGINT NOT NULL AUTO_INCREMENT,
    `plan_title` VARCHAR(50) NULL,
    `plan_date` TIMESTAMP NOT NULL COMMENT '시간까지',
    `plan_content` VARCHAR(255) NULL,
    `plan_scope` ENUM('ALL', 'FRIENDS', 'PRIVATE') NOT NULL COMMENT '전체/친구/비공개',
    PRIMARY KEY (`plan_id`)
);

CREATE TABLE `video` (
    `video_id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(50) NOT NULL,
    `content` VARCHAR(255) NULL,
    `level` ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL COMMENT '홀드 색깔',
    `skill` ENUM('TECHNIQUE1', 'TECHNIQUE2', 'OTHER') NOT NULL COMMENT '기술 명/기타',
    PRIMARY KEY (`video_id`)
);

CREATE TABLE `Favorite` (
    `fav_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `video_id` BIGINT NOT NULL,
    PRIMARY KEY (`fav_id`)
);

CREATE TABLE `Review` (
    `rev_id` BIGINT NOT NULL AUTO_INCREMENT,
    `rev_content` VARCHAR(255) NOT NULL,
    `rev_created_at` TIMESTAMP NOT NULL,
    `rev_updated_at` TIMESTAMP NULL,
    `rev_deleted_at` TIMESTAMP NULL,
    `user_id` BIGINT NOT NULL,
    `video_id` BIGINT NOT NULL,
    PRIMARY KEY (`rev_id`)
);

CREATE TABLE `theClimb` (
    `location_id` BIGINT NOT NULL AUTO_INCREMENT,
    `location_name` VARCHAR(50) NOT NULL,
    `location_wall` VARCHAR(50) NULL,
    `location_color` VARCHAR(50) NOT NULL,
    `video_id` BIGINT NULL,
    PRIMARY KEY (`location_id`)
);

CREATE TABLE `UserPlan` (
    `user_plan_id` BIGINT NOT NULL AUTO_INCREMENT,
    `status` ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL COMMENT '요청 상태',
    `user_id` BIGINT NOT NULL,
    `plan_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_plan_id`)
);

CREATE TABLE `UserFile` (
    `user_file_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `user_file_url` VARCHAR(255) NOT NULL,
    `user_file_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`user_file_id`)
);

CREATE TABLE `Follow` (
    `follow_id` BIGINT NOT NULL AUTO_INCREMENT,
    `follow_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `follow_deleted_at` TIMESTAMP NULL,
    `following_user_id` BIGINT NOT NULL COMMENT '팔로우 요청자',
    `follower_user_id` BIGINT NOT NULL COMMENT '팔로우 받는 사람',
    PRIMARY KEY (`follow_id`),
    UNIQUE KEY `UK_follow_unique` (`following_user_id`, `follower_user_id`)
);

CREATE TABLE `User` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(50) NOT NULL,
    `user_nickname` VARCHAR(50) NOT NULL UNIQUE COMMENT '닉네임(UNIQUE)',
    `password` VARCHAR(255) NOT NULL,
    `social_id` VARCHAR(50) NULL COMMENT '카카오 로그인',
    `user_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_updated_at` TIMESTAMP NULL,
    `user_deleted_at` TIMESTAMP NULL,
    `user_description` VARCHAR(255) NULL,
    `user_img` VARCHAR(255) NOT NULL DEFAULT 'default.png' COMMENT '기본이미지',
    PRIMARY KEY (`user_id`)
);

CREATE TABLE `File` (
    `file_id` BIGINT NOT NULL AUTO_INCREMENT,
    `file_url` VARCHAR(255) NOT NULL,
    `file_name` VARCHAR(255) NOT NULL,
    `video_id` BIGINT NOT NULL,
    PRIMARY KEY (`file_id`)
);

CREATE TABLE `Alarm` (
    `alarm_id` BIGINT NOT NULL AUTO_INCREMENT,
    `alarm_type` ENUM('FOLLOW', 'PLAN') NOT NULL COMMENT 'FOLLOW/PLAN',
    `alarm_related_id` BIGINT NOT NULL,
    `alarm_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `alarm_is_responded` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '응답 여부(0/1)',
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`alarm_id`),
    CONSTRAINT `FK_ALARM_USER` FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`)
);
