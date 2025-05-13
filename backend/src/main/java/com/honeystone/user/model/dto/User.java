package com.honeystone.user.model.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.honeystone.common.dto.video.Video;
import com.honeystone.video.model.type.Level;
import com.honeystone.video.model.type.Skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
@Schema(description = "유저 DTO입니다.")
public class User {
	@Schema(description = "유저 인덱스")
    private Long id;
	
	@Schema(description = "유저 이메일", example = "user@example.com")
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식이 유효하지 않습니다.")
	@Size(max = 50, message = "이메일은 50자 이내여야 합니다.")
	private String email;
	
	@Schema(description = "유저 닉네임")
	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(max = 50, message = "닉네임은 50자 이내여야 합니다.")
	private String nickname;
	
	@Schema(description = "유저 비밀번호")
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(max = 255, message = "비밀번호는 255자 이내여야 합니다.")
	private String password;
	
	@Schema(description = "생성 시각", example = "2025-05-11T18:45:00")
	private LocalDateTime created_at;
	
	@Schema(description = "업데이트 시각", example = "2025-05-11T18:45:00")
	private LocalDateTime updated_at;
	
	@Schema(description = "삭제 시각", example = "2025-05-11T18:45:00")
	private LocalDateTime deleted_at;
	
	@Schema(description = "유저 자기소개")
	@Size(max = 255, message = "소개글은 255자 이내여야 합니다.")
	private String description;
	
	@Schema(description = "유저 프로필 이미지")
	private String img;

}
