package com.honeystone.common.dto.video;

import com.honeystone.video.model.type.Level;
import com.honeystone.video.model.type.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비디오 및 사진 게시물 DTO입니다.")
public class Video {
    @Schema(description = "게시물 인덱스", example = "1")
    private Long id;

    @Schema(description = "게시물 제목", example = "논현점 파랑이 정복")
    @NotNull
    private String title;

    @Schema(description = "게시물 상세 내용", example = "오늘 논현점 파랑이 완전 정복했어요!!")
    private String description;

    @Schema(description = "난이도", example = "RED")
    @NotNull
    private Level level;

    @Schema(description = "기술명", example = "PINCH")
    @NotNull
    private Set<Skill> skill;

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
