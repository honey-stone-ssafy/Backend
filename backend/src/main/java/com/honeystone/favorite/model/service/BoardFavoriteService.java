package com.honeystone.favorite.model.service;

import com.honeystone.common.security.MyUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.honeystone.common.dto.board.GetBoard;

public interface BoardFavoriteService {

	Page<GetBoard> getFavoriteBoardList(Long userId, Pageable pageable);
	void addFavorite(MyUserPrincipal user, Long boardId);
    void removeFavorite(MyUserPrincipal user, Long boardId);

}
