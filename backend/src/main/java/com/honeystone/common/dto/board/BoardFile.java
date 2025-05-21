package com.honeystone.common.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시물 업로드 시 저장할 파일 DTO입니다.")
public class BoardFile {
    @Schema(description = "파일 인덱스", example = "1")
    private Long fileId;

    @Schema(description = "파일 URL", example = "https://cdn.example.com/boards/1/file.png")
    private String url;

    @Schema(description = "파일명", example = "file.png")
    private String filename;

    @Schema(description = "연결된 비디오 인덱스", example = "1")
    private Long boardId;

    @Override
    public String toString() {
        return "File{" +
            "fileId=" + fileId +
            ", url='" + url + '\'' +
            ", filename='" + filename + '\'' +
            ", boardId=" + boardId +
            '}';
    }
}
