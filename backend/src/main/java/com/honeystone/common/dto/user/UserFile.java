package com.honeystone.common.dto.user;

import com.honeystone.common.dto.board.BoardFile;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "프로필 이미지 업로드 시 저장할 파일 DTO입니다.")
public class UserFile {
	
	@Schema(description = "파일 인덱스", example = "1")
    private Long fileId;

    @Schema(description = "파일 URL", example = "https://cdn.example.com/users/1/file.png")
    private String url;

    @Schema(description = "파일명", example = "file.png")
    private String filename;

    @Schema(description = "연결된 유저 인덱스", example = "1")
    private Long userId;
}
