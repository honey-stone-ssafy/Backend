package com.honeystone.board.model.dao;

import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.board.BoardFile;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import org.apache.ibatis.annotations.Mapper;

import com.honeystone.common.dto.board.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

@Mapper
public interface BoardDao {

	public List<GetBoard> getBoardList(@Param("search") SearchBoardCondition search, @Param("pageable") Pageable pageable);

	public void createBoard(Board video);

	public void createFile(BoardFile file);

	public void updateBoard(Board video);

	public int existsById(Long id); // id로 게시물 조회

	public void deleteBoard(Long id);

	public void completeDeleteBoard(Long id);

	public void deleteFile(Long id);

	public GetBoard getBoard(Long id);

	public List<Board> findBoardsToDelete();

	public long countBoards(SearchBoardCondition search);
}
