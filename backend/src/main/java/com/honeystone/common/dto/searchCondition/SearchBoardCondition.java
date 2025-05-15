package com.honeystone.common.dto.searchCondition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBoardCondition {
	private String keyword; // 검색어(제목, 내용)
	private String orderBy; // 최신순, 찜 많은 순
	
	@Override
	public String toString() {
		return "SearchBoardCondition [keyword=" + keyword + ", orderBy=" + orderBy + "]";
	}
	
}
