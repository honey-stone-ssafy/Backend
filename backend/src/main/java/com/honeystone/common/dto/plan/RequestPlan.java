package com.honeystone.common.dto.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request-Plan DTO 입니다.")
public class RequestPlan {
    @Schema(description = "사용자-일정 매핑 ID", example = "1")
    private Long id;

    @Schema(description = "요청 상태", example = "PENDING", allowableValues = {"PENDING", "ACCEPTED", "REJECTED"})
    private String status;

    @Schema(description = "사용자 ID", example = "42")
    private Long userId;

    @Schema(description = "일정(plan) ID", example = "101")
    private Long planId;

    @Schema(description = "역할", example = "OWNER", allowableValues = {"OWNER", "PARTICIPANT"})
    private String role;

    @Schema(description = "생성 일시", example = "2025-05-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-05-02T12:30:00", nullable = true)
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 일시", example = "2025-05-10T08:00:00", nullable = true)
    private LocalDateTime deletedAt;
}
