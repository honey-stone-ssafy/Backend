package com.honeystone.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.honeystone.common.dto.board.Board;
import com.honeystone.common.dto.board.BoardFile;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.theClimb.TheClimb;

@Mapper
public interface BoardDao {

	 List<GetBoard> getBoardList(@Param("search") SearchBoardCondition search, @Param("pageable") Pageable pageable);

	 void createBoard(Board video);

	 void createFile(BoardFile file);
	
	 Long findTheClimb(TheClimb theClimb);
	
	 void createTheClimbBoard(@Param("boardId") Long boardId, @Param("theClimbId") Long theClimbId);

	 void updateTheClimbBoard(@Param("boardId") Long boardId, @Param("theClimbId") Long theClimbId);

	 void updateBoard(Board video);

	 int existsById(Long id); // id로 게시물 조회

	 void deleteBoard(Long id);

	 void completeDeleteBoard(Long id);

	 void deleteFile(Long id);

	 GetBoard getBoard(Long id);

	 List<Board> findBoardsToDelete();

	 long countBoards(SearchBoardCondition search);

	 void deleteTheClimbBoard(Long boardId);
}
