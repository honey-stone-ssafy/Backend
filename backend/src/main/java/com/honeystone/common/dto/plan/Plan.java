package com.honeystone.common.dto.plan;

import com.honeystone.plan.model.type.Scope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Plan DTO 입니다.")
public class Plan {
    @Schema(description = "일정 ID", example = "1")
    private Long id;

    @Schema(description = "일정 제목", example = "논현점 클라이밍 모임")
    private String title;

    @Schema(description = "일정 시작 시간", example = "2025-05-15T18:00:00")
    private LocalDateTime start;

    @Schema(description = "일정 종료 시간", example = "2025-05-15T20:00:00")
    private LocalDateTime end;

    @Schema(description = "메모", example = "스트레칭 먼저 하기!")
    private String memo;

    @Schema(description = "운동 지점", example = "강남", nullable = true)
    private String location;

    @Schema(description = "공개 범위", example = "FRIENDS", allowableValues = {"ALL", "FRIENDS", "PRIVATE"})
    private Scope scope;

    @Schema(description = "일정 생성 시간", example = "2025-05-01T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "일정 수정 시간", example = "2025-05-10T09:00:00", nullable = true)
    private LocalDateTime updatedAt;

    @Schema(description = "일정 삭제 시간", example = "2025-05-20T12:00:00", nullable = true)
    private LocalDateTime deletedAt;

    @Schema(description = "작성자 유저 ID", example = "42")
    private Long userId;

    @Override
    public String toString() {
        return "Plan{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", start=" + start +
            ", end=" + end +
            ", memo='" + memo + '\'' +
            ", location='" + location + '\'' +
            ", scope=" + scope +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deletedAt=" + deletedAt +
            ", userId=" + userId +
            '}';
    }
}
