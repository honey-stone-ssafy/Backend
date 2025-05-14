package com.honeystone.board.model.dao;

import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.board.BoardFile;
import org.apache.ibatis.annotations.Mapper;

import com.honeystone.common.dto.board.Board;

@Mapper
public interface BoardDao {

	public List<Board> selectAll();

	public void createBoard(Board video);

	public void createFile(BoardFile file);

	public void updateBoard(Board video);

	public int existsById(Long id); // id로 게시물 조회

	public void deleteBoard(Long id);

	public void completeDeleteBoard(Long id);

	public void deleteFile(Long id);

	public GetBoard getBoard(Long id);

	public List<Board> findBoardsToDelete();
}
