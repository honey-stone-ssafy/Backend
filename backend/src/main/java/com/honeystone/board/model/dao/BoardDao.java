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
import org.springframework.security.core.parameters.P;

@Mapper
public interface BoardDao {

	 List<GetBoard> getBoardList(@Param("userId") Long userId, @Param("search") SearchBoardCondition search, @Param("pageable") Pageable pageable);

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

	 GetBoard getBoard(@Param("userId") Long userId, @Param("id") Long id);

	 List<Board> findBoardsToDelete();

	 long countBoards(SearchBoardCondition search);

	 void deleteTheClimbBoard(Long boardId);

	BoardFile getBoardFile(Long boardId);
	void deleteFileById(Long fileId);  // 특정 파일 삭제 (수정용)
	void deleteFileByBoardId(Long boardId);  // 게시글의 모든 파일 삭제 (영구삭제용)
}
