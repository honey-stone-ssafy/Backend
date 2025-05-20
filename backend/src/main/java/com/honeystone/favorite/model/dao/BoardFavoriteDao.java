package com.honeystone.favorite.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.honeystone.common.dto.board.GetBoard;

@Mapper
public interface BoardFavoriteDao {

	public long countBoards(@Param("userId") Long userId);

	List<GetBoard> getBoardList(@Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("size") int size);

	public void insertFavorite(Long userId, Long boardId);

	public void deleteFavorite(Long userId, Long boardId);

}
