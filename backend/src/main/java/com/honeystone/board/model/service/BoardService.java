package com.honeystone.board.model.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.honeystone.common.dto.board.Board;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.theClimb.TheClimb;

public interface BoardService {
	public Page<GetBoard> getBoardList(SearchBoardCondition search, Pageable pageable);

	public void createBoard(String userEmail, Board board, MultipartFile file) throws IOException;

	public void updateBoard(String userEmail, Long id, Board board);

	public void deleteBoard(String userEmail, Long id);

	public GetBoard getBoard(Long id);
}
