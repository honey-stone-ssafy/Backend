package com.honeystone.common.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입용 유저 DTO입니다.")
public class UserSignupRequest {
	
	@Schema(description = "유저 이메일", example = "user@example.com")
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식이 유효하지 않습니다.")
	@Size(max = 50, message = "이메일은 50자 이내여야 합니다.")
	private String email;
	
	@Schema(description = "유저 닉네임", example = "chuchu")
	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이내여야 합니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다.")
	private String nickname;
	
	@Schema(description = "유저 비밀번호", example = "Qwer1234!")
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 8, max = 255, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	@Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
	        message = "비밀번호는 대소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."
	    )
	private String password;
}
