package com.honeystone.common.dto.searchCondition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 검색 조건 DTO")
public class SearchBoardCondition {

    @Schema(description = "검색어 (제목 또는 내용 기준)", example = "볼더링")
    private String keyword;

    @Schema(description = "정렬 기준 (예: 'created_at' 또는 'favorites')", example = "created_at")
    private String orderBy;

    @Override
    public String toString() {
        return "SearchBoardCondition [keyword=" + keyword + ", orderBy=" + orderBy + "]";
    }
}
