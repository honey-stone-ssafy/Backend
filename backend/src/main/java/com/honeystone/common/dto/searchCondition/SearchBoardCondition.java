package com.honeystone.common.dto.searchCondition;

import java.util.List;

import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Location;
import com.honeystone.board.model.type.Skill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 검색 조건 DTO")
public class SearchBoardCondition {

    @Schema(description = "검색어 (제목 또는 내용에서 검색)", example = "볼더링")
    private String keyword;

    @Schema(description = "지점 필터 (복수 선택 가능), 예: locations=GANGNAM&locations=HONGDAE", example = "GANGNAM")
    private List<Location> locations;

    @Schema(description = "난이도 필터 (복수 선택 가능), 예: levels=RED&levels=BLUE", example = "RED")
    private List<Level> levels;

    @Schema(description = "기술명 필터(복수 선택 가능, 예: skills=PINCH&skills=SLOPER)",
    	    example = "PINCH")
    private List<Skill> skills;

    @Schema(description = "정렬 기준 (created_at: 최신순, favorites: 찜 많은 순)", example = "created_at")
    private String orderBy;

    @Override
    public String toString() {
        return "SearchBoardCondition [keyword=" + keyword + ", locations=" + locations + ", levels=" + levels + ", skills=" + skills + ", orderBy=" + orderBy + "]";
    }
}
