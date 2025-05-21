package com.honeystone.common.dto.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Location;
import com.honeystone.board.model.type.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "비디오 상세 조회용 DTO (파일 정보 포함)")
public class GetBoard {
    @Schema(description = "게시물 인덱스")
    private Long id;

    @Schema(description = "게시물 제목", example = "논현점 파랑이 정복")
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
    private String title;

    @Schema(description = "게시물 상세 내용", example = "오늘 논현점 파랑이 완전 정복했어요!!")
    private String description;

    @Schema(description = "난이도", example = "RED")
    @Enumerated(EnumType.STRING)
    private Level level;

    @Schema(description = "기술명", example = "[\"PINCH\", \"SLOPER\"]")
    @Enumerated(EnumType.STRING)
    private Set<Skill> skill;

    @Schema(description = "장소", example = "HONGDAE")
    @Enumerated(EnumType.STRING)
    private Location location;

    @Schema(description = "생성 시각", example = "2025-05-11T18:45:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시각", example = "2025-05-11T19:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 시각", example = "2025-05-11T19:00:00")
    private LocalDateTime deletedAt;

    @Schema(description = "파일 인덱스", example = "1")
    private Long fileId;

    @Schema(description = "파일 URL", example = "https://cdn.example.com/videos/1/file.png")
    private String url;

    @Schema(description = "파일명", example = "file.png")
    private String filename;
}
