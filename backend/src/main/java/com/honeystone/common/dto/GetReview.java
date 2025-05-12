package com.honeystone.common.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class GetReview {
	 @Schema(description = "리뷰 ID", example = "1")
	    private Long id;

	    @Schema(description = "리뷰 내용", example = "이 영상 정말 도움이 되었어요!")
	    private String content;

	    @Schema(description = "리뷰 작성일시", example = "2025-05-12T14:30:00")
	    private LocalDateTime createdAt;

	    @Schema(description = "리뷰 수정일시", example = "2025-05-12T15:00:00", nullable = true)
	    private LocalDateTime updatedAt;

	    @Schema(description = "리뷰 삭제일시", example = "2025-05-13T10:00:00", nullable = true)
	    private LocalDateTime deletedAt;

	    @Schema(description = "작성자 유저 ID", example = "42")
	    private Long userId;

	    @Schema(description = "연결된 영상 ID", example = "101")
	    private Long videoId;
}
