package com.honeystone.common.dto.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "게시물 DTO입니다.")
public class Board {
    @Schema(description = "게시물 인덱스")
    private Long id;

    @Schema(description = "게시물 제목", example = "논현점 파랑이 정복")
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
    private String title;

    @Schema(description = "게시물 상세 내용", example = "오늘 논현점 파랑이 완전 정복했어요!!")
    @Size(max = 255, message = "내용은 255자 이내여야 합니다.")
    private String description;

    @Schema(description = "난이도", example = "RED")
    @NotNull(message = "난이도는 필수입니다.")
    private Level level;

    @Schema(description = "기술명", example = "[\"PINCH\", \"SLOPER\"]")
    private Set<Skill> skill;

    @Schema(description = "생성 시각", example = "2025-05-11T18:45:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시각", example = "2025-05-11T19:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 시각", example = "2025-05-11T19:00:00")
    private LocalDateTime deletedAt;

    @Schema(description = "첨부 파일", type = "string", format = "binary")
    @NotNull(message = "파일 첨부는 필수입니다.")
    private MultipartFile file;

    @Override
    public String toString() {
        return "Video{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", level='" + level + '\'' +
            ", skill='" + skill + '\'' +
            '}';
    }
}
