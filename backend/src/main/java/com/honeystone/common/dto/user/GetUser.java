package com.honeystone.common.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 조회용 DTO입니다.")
public class GetUser {
	
	@Schema(description = "유저 인덱스")
    private Long id;
	
	@Schema(description = "유저 이메일", example = "user@example.com")
	private String email;
	
	@Schema(description = "유저 닉네임")
	private String nickname;
	
	@Schema(description = "유저 자기소개")
	private String description;
	
	@Schema(description = "유저 프로필 이미지")
	private String img;
	
//	@Schema(description = "프로필 이미지 인덱스", example = "1")
//    private Long fileId;
//
//    @Schema(description = "프로필 이미지 파일 URL", example = "https://cdn.example.com/videos/1/file.png")
//    private String url;
//
//    @Schema(description = "프로필 이미지 파일명", example = "file.png")
//    private String filename;
    
    @Schema(description = "팔로잉 유무")
	private Boolean isFollowing;
 
}
