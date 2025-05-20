package com.honeystone.favorite.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.honeystone.common.dto.board.GetBoard;

public interface BoardFavoriteService {

	Page<GetBoard> getFavoriteBoardList(Long userId, Pageable pageable);
	void addFavorite(String userEmail, Long userId, Long boardId);
    void removeFavorite(String userEmail, Long userId, Long boardId);

}
