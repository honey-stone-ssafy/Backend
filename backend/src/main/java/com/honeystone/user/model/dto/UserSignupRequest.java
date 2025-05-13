package com.honeystone.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Schema(description = "회원가입용 유저 DTO입니다.")
public class UserSignupRequest {
	
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
}
